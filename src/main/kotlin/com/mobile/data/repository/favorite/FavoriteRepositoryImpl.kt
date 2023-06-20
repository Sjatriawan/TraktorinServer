package com.mobile.data.repository.favorite

import com.mobile.data.models.Favorite
import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.response.FavoriteResponse
import com.mobile.response.PostResponse
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.facet
import org.litote.kmongo.util.idValue

class FavoriteRepositoryImpl(
    private val db: CoroutineDatabase
) : FavoriteRepository {
    private val favoriteCollection = db.getCollection<Favorite>()
    private val users = db.getCollection<User>()
    override suspend fun getFavoritesForUser(userId: String, page: Int, pageSize: Int): List<FavoriteResponse> {
        return favoriteCollection.find(Favorite::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map { favorite ->
                val user = users.findOneById(favorite.userId)
                FavoriteResponse(
                    userId = userId,
                    id = favorite.id,
                    fullname = favorite.fullname ?: "",
                    price = favorite.price.toString(),
                    village = favorite.village
                )
            }
    }

    override suspend fun addFavorite(favorite: Favorite): Boolean {
            favoriteCollection.insertOne(favorite)
            return true
    }

    override suspend fun removeFavorite(userId: String, postId: String): Boolean {
        val result = favoriteCollection.deleteOne(
            Favorite::userId eq userId,
            Favorite::postId eq postId
        )
        return result.deletedCount > 0
    }
}
