package com.mobile.data.repository.favorite

import com.mobile.data.models.Favorite
import com.mobile.response.FavoriteResponse
import com.mobile.response.PostResponse

interface FavoriteRepository {


        suspend fun getFavoritesForUser(
                userId:String,
                page:Int,
                pageSize:Int
        ):List<FavoriteResponse>
        suspend fun addFavorite(favorite: Favorite): Boolean
        suspend fun removeFavorite(userId: String, postId: String): Boolean

}