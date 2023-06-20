package com.mobile.services

import com.mobile.data.models.Favorite
import com.mobile.data.models.Post
import com.mobile.data.repository.favorite.FavoriteRepository
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.CreateFavoriteRequest
import com.mobile.response.BasicApiResponse
import com.mobile.response.FavoriteResponse
import com.mobile.response.PostResponse
import com.mobile.util.Constant

class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val post: PostRepository
) {
    suspend fun addFavorite(request: CreateFavoriteRequest, userId: String): Boolean {
        val get = post.getPost(request.postId)
        return favoriteRepository.addFavorite(
            Favorite(
                postId = request.postId,
                userId = userId,
                price = get?.price ?: 0.0,
                fullname = get?.fullname ?: "",
                village = get?.village ?: ""
            )
        )
    }

    suspend fun removeFavorite(postId: String, userId: String): BasicApiResponse<Unit> {
        return if (favoriteRepository.removeFavorite(userId, postId)) {
            BasicApiResponse(
                successful = true
            )
        } else {
            BasicApiResponse(
                successful = false
            )
        }
    }

    suspend fun getFavoriteForUser(
        userId:String,
        page:Int = 0,
        pageSize:Int = Constant.DEFAULT_POST_PAGE_SIZE
    ):List<FavoriteResponse> {
        return favoriteRepository.getFavoritesForUser(userId,page,pageSize)
    }
}
