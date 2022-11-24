package com.mobile.data.repository.post

import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.response.PostResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.regex

class PostRepositoryImpl(
    db:CoroutineDatabase
):PostRepository {
    private val posts = db.getCollection<Post>()
    private val users = db.getCollection<User>()

    override suspend fun createPostIfUserExist(post: Post):Boolean {
        val doesUserExist = users.findOneById(post.userId) != null
        if (!doesUserExist ){
            return false
        }
        posts.insertOne(post)
        return true
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostForHome(
        ownUserId:String,
        page:Int,
        pageSize:Int
    ):List<PostResponse> {
        return posts.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                    val user = users.findOneById(post.userId)
                    PostResponse(
                        id = post.id,
                        userId = ownUserId,
                        imageUrl = post.imageUrl,
                        description = post.description,
                        isOwnPost = ownUserId == post.userId,
                        fullname = user?.fullname ?: "",
                        price = post.price,
                        village = post.village,
                        district = post.district,
                        province = post.province,
                    )
            }
    }

    override suspend fun getPostForProfile(ownUserId:String,userId: String, page: Int, pageSize: Int): List<PostResponse> {
        val user = users.findOneById(userId) ?: return emptyList()
            return posts.find(Post::userId eq userId)
                .skip(page * pageSize)
                .limit(pageSize)
                .descendingSort(Post::timestamp)
                .toList()
                .map {post ->
                    PostResponse(
                        id = post.id,
                        userId = userId,
                        imageUrl = post.imageUrl,
                        description = post.description,
                        isOwnPost = ownUserId == post.userId,
                        fullname = user?.fullname ?: "",
                        price = post.price,
                        village = post.village,
                        district = post.district,
                        province = post.province,
                    )
                }
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }

    override suspend fun searchPostWithServiceBy(query: String): List<Post> {
        return posts.find(
            Post::fullname regex Regex("(?i).*$query.*")
        ).toList()
    }
}
