package com.mobile.services

import com.mobile.data.repository.favorite.FavoriteRepository

class FavoriteService (
    private val favoriteRepository: FavoriteRepository
    ){

    suspend fun likePost(userId:String, parentId:String):Boolean{
        return favoriteRepository.likeParents(userId, parentId)
    }

    suspend fun unlikePost(userId: String,parentId: String):Boolean{
        return favoriteRepository.unlikeParents(userId,parentId)
    }

}