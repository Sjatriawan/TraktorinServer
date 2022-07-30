package com.mobile.routes

import com.mobile.data.request.CreatePostRequest
import com.mobile.data.request.DeletePostRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.QueryParams
import com.mobile.util.ifEmailBelongsToUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
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
}

fun Route.getListPostRoute(
    postService: PostService,
){
    authenticate {
        get("api/post/get"){
            val posts = postService.getAllPost()
            call.respond(
                HttpStatusCode.OK, posts
            )
        }

    }
}

fun Route.deletePostRoute(
    postService: PostService,
    userService: UserService
){
    authenticate {
        delete("api/post/delete"){
            val request = call.receiveOrNull<DeletePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val post = postService.getPost(request.postId)
            if (post == null){
                call.respond(
                    HttpStatusCode.NotFound
                )
                return@delete
            }
            ifEmailBelongsToUser(
                userId = post.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
                postService.deletePost(postId = request.postId)
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}


