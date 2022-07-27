package com.mobile.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mobile.data.repository.user.UserRepository
import com.mobile.data.models.User
import com.mobile.data.request.CreateAccountRequest
import com.mobile.data.request.LoginRequest
import com.mobile.response.AuthResponse
import com.mobile.response.BasicApiResponse
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.ApiResponseMessages.FIELDS_BLANK
import com.mobile.util.ApiResponseMessages.USER_ALREADY_EXIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createUserRoutes(userService: UserService) {
     post("api/user/create") {
      val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
          call.respond(HttpStatusCode.BadRequest)
          return@post
      }
         if(userService.doesUserWithEmailExist(request.email)){
             call.respond(
                 BasicApiResponse(false, USER_ALREADY_EXIST)
             )
             return@post
         }

         when(userService.validateCreateAccount(request)){
             is UserService.ValidationEvent.ErrorFieldEmpty -> {
                 call.respond(
                     BasicApiResponse(
                         false,
                         FIELDS_BLANK )
                 )
             }
             is UserService.ValidationEvent.Success ->{
                 userService.createUser(request)
                 call.respond(
                     BasicApiResponse(
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

        val isPasswordCorrect = userService.doesPasswordMatchForUser(request)
        if (isPasswordCorrect){
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim("email", request.email)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(token = token)
            )
        }else{
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(successful = false,message = ApiResponseMessages.INCORRECT_PASSWORD)
            )
        }
    }
}