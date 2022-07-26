package com.mobile.data.repository.post

import com.mobile.data.models.Post
import com.mobile.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne

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
        posts.deleteOneById(postId )
    }

    override suspend fun getPost(userId: String){
        posts.findOneById(posts)
    }


}