package com.mobile.data.repository.favorite

interface FavoriteRepository {

    suspend fun likeParents(userId:String, postId:String):Boolean

    suspend fun unlikeParents(userId: String,postId: String):Boolean

}