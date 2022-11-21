package com.mobile.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import com.mobile.data.request.CreateAccountRequest
import com.mobile.data.request.LoginRequest
import com.mobile.data.request.UpdateProfileRequest
import com.mobile.response.AuthResponse
import com.mobile.response.BasicApiResponse
import com.mobile.services.UserService
import com.mobile.util.*
import com.mobile.util.ApiResponseMessages.FIELDS_BLANK
import com.mobile.util.ApiResponseMessages.USER_ALREADY_EXIST
import com.mobile.util.Constant.BASE_URL
import com.mobile.util.Constant.PROFILE_PICTURE_PATH
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*


fun Route.createUserRoutes(userService: UserService) {
     post("api/user/create") {
      val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
          call.respond(HttpStatusCode.BadRequest)
          return@post
      }
         if(userService.doesUserWithEmailExist(request.email)){
             call.respond(
                 BasicApiResponse<Unit>(false, USER_ALREADY_EXIST)
             )
             return@post
         }

         when(userService.validateCreateAccount(request)){
             is UserService.ValidationEvent.ErrorFieldEmpty -> {
                 call.respond(
                     BasicApiResponse<Unit>(
                         false,
                         FIELDS_BLANK )
                 )
             }
             is UserService.ValidationEvent.Success ->{
                 userService.createUser(request)
                 call.respond(
                     BasicApiResponse<Unit>(
                         successful = true
                     )
                 )
             }
         }
     }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret:String
){
    post("api/user/login"){
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if(request.email.isBlank() ||  request.password.isBlank()) {
            call.respond(
                HttpStatusCode.BadRequest
            )
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = "Invalid Credentials")
            )
            return@post
        }

        val isPasswordCorrect = userService.doesPasswordMatchForUser(request)
        if (isPasswordCorrect){
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = AuthResponse(token = token)
                )
            )
        }else{
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = false,message = ApiResponseMessages.INCORRECT_PASSWORD)
            )
        }
    }
}

fun Route.updateUserRoutes(userService: UserService) {
    val gson: Gson by inject()
    authenticate {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePictureFileName: String? = null
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "update_profile_data") {
                            updateProfileRequest = gson.fromJson(
                                part.value,
                                UpdateProfileRequest::class.java
                            )
                        }
                    }
                    is PartData.FileItem -> {
                        profilePictureFileName = part.save(PROFILE_PICTURE_PATH)
                    }
                    else -> {}
                }
            }

            val profilePictureUrl = "${BASE_URL}profile_pictures/$profilePictureFileName"

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateProfile(
                    userId = call.userId,
                    profileImgUrl = profilePictureUrl,
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${Constant.PROFILE_PICTURE_PATH}/$profilePictureFileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}

//fun Route.updateUserRoutes(
//    userService: UserService
//){
//    val gson:Gson by inject()
//    authenticate{
//        put("/api/user/update") {
//            val multipart = call.receiveMultipart()
//            var updateProfileRequest:UpdateProfileRequest? = null
//            var fileName:String? = null
//            multipart.forEachPart { partData ->
//                when(partData){
//                    is PartData.FormItem ->{
//                        if (partData.name == "update_profile_data"){
//                            updateProfileRequest = gson.fromJson(
//                                partData.value,
//                                UpdateProfileRequest::class.java
//                            )
//                        }
//                    }
//                    is PartData.FileItem -> {
//                       fileName = partData.save(PROFILE_PICTURE_PATH)
//                    }
//                    is PartData.BinaryItem -> Unit
//                    is PartData.BinaryChannelItem -> Unit
//                }
//            }
//            val profilePictureUrl = "${BASE_URL}profile_pictures/$fileName"
//
//            updateProfileRequest?.let { request ->
//                val updateAcknowledged = userService.updateProfile(
//                    userId = call.userId,
//                    profileImgUrl = profilePictureUrl,
//                    updateProfileRequest = request
//                )
//                if (updateAcknowledged){
//                    call.respond(
//                        HttpStatusCode.OK,
//                        BasicApiResponse<Unit>(
//                            successful = true
//                        )
//                    )
//                }else{
//                    File("${PROFILE_PICTURE_PATH}/$fileName").delete()
//                    call.respond(HttpStatusCode.InternalServerError)
//                }
//            } ?: kotlin.run {
//                call.respond(HttpStatusCode.BadRequest)
//                return@put
//            }
//        }
//    }
//}

fun Route.getUserProfile(userService: UserService){
    authenticate{
        get("/api/user/profile"){
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            if (userId == null || userId.isBlank()){
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@get
            }
            val profileResponse = userService.getUserProfile(userId, call.userId)
        }
    }
}

fun Route.authenticate(){
    authenticate {
        get("api/user/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}