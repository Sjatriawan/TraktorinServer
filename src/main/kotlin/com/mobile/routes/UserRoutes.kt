package com.mobile.routes

import com.mobile.data.repository.user.UserRepository
import com.mobile.data.models.User
import com.mobile.data.request.CreateAccountRequest
import com.mobile.response.BasicApiResponse
import com.mobile.util.ApiResponseMessages.FIELDS_BLANK
import com.mobile.util.ApiResponseMessages.USER_ALREADY_EXIST
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoutes(userRepository: UserRepository) {
    route("/api/user/create"){
     post {
      val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
          call.respond(HttpStatusCode.BadRequest)
          return@post
      }

         val userExist = userRepository.getUserByEmail(request.email) != null
         if(userExist){
             call.respond(
                 BasicApiResponse(false, USER_ALREADY_EXIST)
             )
             return@post
         }

         if(request.email.isBlank() || request.username.isBlank() || request.password.isBlank()) {
             call.respond(
                 BasicApiResponse(false,FIELDS_BLANK )
             )
             return@post
         }
         userRepository.createUser(
             User(
                 email = request.email,
                 username = request.username,
                 password = request.password,
                 address = "Pohgading",
                 userPhone = 0,
                 profileImgUrl = "",
                 lat = 0.0,
                 lng = 0.0,
                 isService = false,
             )
         )
         call.respond(
             BasicApiResponse(
                 true
             )
         )
     }
    }
}