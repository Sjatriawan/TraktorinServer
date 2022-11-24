package com.mobile.routes

import com.google.gson.Gson
import com.mobile.data.models.Post
import com.mobile.data.request.CreatePostRequest
import com.mobile.data.request.DeletePostRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.koin.ktor.ext.inject
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.log

fun Route.createPostRoutes(
    postService: PostService,
) {
    val gson by inject<Gson>()
    authenticate {
        post("/api/post/create") {
            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        val name = part.name
                        if(name == "post_data"){
                            createPostRequest = gson.fromJson(
                                part.value,
                                CreatePostRequest::class.java
                            )
                        }
                    }
                    is PartData.FileItem -> {
                        fileName = part.save(Constant.POST_PICTURE_PATH)
                    }
                    else -> {}
                }
            }

            val postPictureUrl = "${Constant.BASE_URL}post_pictures/$fileName"
            createPostRequest?.let { request ->
                val createPostAcknowledged = postService.createPostIfUserExist(
                    request = request,
                    userId = call.userId,
                    imageUrl = postPictureUrl
                )
                if (createPostAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${Constant.POST_PICTURE_PATH}/$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)

                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post

            }
        }
    }
}

fun Route.getListPostRoute(
   postService: PostService,
){
    authenticate {
        get("/api/post/get"){
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: 0
            val posts = postService.getPostForHome(call.userId,page,pageSize)

            call.respond(HttpStatusCode.OK, posts)
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
            if (post.userId == call.userId) {
                postService.deletePost(request.postId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

    }
}

fun Route.searchPost(
    postService: PostService
){
  authenticate {
      get("api/post/search") {
          val query = call.parameters[QueryParams.PARAM_QUERY]
          if (query.isNullOrBlank()){
              call.respond(
                  HttpStatusCode.OK,
                  listOf<Post>()
              )
              return@get
          }
          val searchResult = postService.searchPostByServiced(query)
          call.respond(
              HttpStatusCode.OK,
              searchResult
          )

      }
  }
}


