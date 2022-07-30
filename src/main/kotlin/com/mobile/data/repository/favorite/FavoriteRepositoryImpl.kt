package com.mobile.data.repository.favorite

import com.mobile.data.models.Favorite
import com.mobile.data.models.User
import com.mobile.services.FavoriteService
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class FavoriteRepositoryImpl(
    db:CoroutineDatabase
):FavoriteRepository{
    private val favorite = db.getCollection<Favorite>()
    private val users = db.getCollection<User>()
    override suspend fun likeParents(userId: String, postId: String): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist){
            favorite.insertOne(Favorite(
                userId,postId
            ))
            true
        }else
            false

    }

    override suspend fun unlikeParents(userId: String, parentId: String): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist){
            favorite.deleteOne(
                and(
                    Favorite::userId eq userId,
                    Favorite::parentId eq parentId
                )
            )
            true
        }else
            false
    }
}