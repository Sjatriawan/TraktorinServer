package com.mobile.routes

import com.mobile.data.request.DeleteFavoriteRequest
import com.mobile.data.request.CreateFavoriteRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.FavoriteService
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.QueryParams
import com.mobile.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addFavoriteRoute(
    favoriteService: FavoriteService,
    userService: UserService
){
    authenticate {
        post("api/favorite/add") {
            val userId = call.userId
            val request = call.receiveOrNull<CreateFavoriteRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val response = favoriteService.addFavorite(request, userId)
            call.respond(response)
        }

    }
}

fun Route.getListFavoriteRoute(
    favoriteService: FavoriteService
){
    authenticate {
        get("/api/favorite/get"){
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: 0
            val posts = favoriteService.getFavoriteForUser(call.userId,page,pageSize)
            call.respond(HttpStatusCode.OK, posts)
        }
    }
}




//fun Route.deleteFavoriteRoute(
//    favoriteService: FavoriteService,
//    userService: UserService
//){
//    authenticate {
//        delete ("api/favorite/delete") {
//            val request = call.receiveOrNull<DeleteFavoriteRequest>() ?: kotlin.run {
//                call.respond(HttpStatusCode.BadRequest)
//                return@delete
//            }
//                val deleteFavoriteSuccess = favoriteService.removeFavorite(postId = request.postId)
//                if (deleteFavoriteSuccess){
//                    call.respond(
//                        HttpStatusCode.OK,
//                        BasicApiResponse<Unit>(
//                            successful = true
//                        )
//                    )
//                }else{
//                    call.respond(
//                        HttpStatusCode.OK,
//                        BasicApiResponse<Unit>(
//                            successful = false,
//                            message = ApiResponseMessages.USER_NOT_FOUND
//                        )
//                    )
//                }
//        }
//    }
//}