package com.mobile.routes

import com.mobile.data.request.CreatePostRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        post("api/post/create"){
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
            val isEmailByUser = userService.doesEmailBelongToUserId(
                    email = email ?: "",
                    userId = request.userId
                )

            //BUG MERESAHKAN
            if (!isEmailByUser){
                call.respond(HttpStatusCode.Unauthorized, "You are monkey")
                return@post
            }

            val didUserExist = postService.createPostIfUserExist(request)
            if (!didUserExist){
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            }
        }
    }


}