package com.mobile.routes

import com.mobile.data.models.Favorite
import com.mobile.data.models.User
import com.mobile.data.request.DeleteFavoriteRequest
import com.mobile.data.request.FavoriteRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.FavoriteService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.QueryParams
import com.mobile.util.ifEmailBelongsToUser
import io.ktor.client.request.*
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
        post("api/favorite") {
            val request = call.receiveOrNull<FavoriteRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){

                val addFavoriteSuccess =favoriteService.likePost(userId = request.userId, parentId = request.parentId)
                if (addFavoriteSuccess){
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                }else{
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }

    }
}

fun Route.deleteFavoriteRoute(
    favoriteService: FavoriteService,
    userService: UserService
){
    authenticate {
        delete ("api/favorite") {
            val request = call.receiveOrNull<DeleteFavoriteRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){

                val deleteFavoriteSuccess =favoriteService.unlikePost(userId = request.userId, parentId = request.parentId)
                if (deleteFavoriteSuccess){
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                }else{
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}