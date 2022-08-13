package com.mobile.data.repository.post

import com.mobile.data.models.Post
import com.mobile.data.models.User

interface PostRepository {

    suspend fun createPostIfUserExist(post: Post):Boolean

    suspend fun deletePost(postId:String)

    suspend fun getListPost():List<Post>

    suspend fun getPost(postId: String):Post?

    suspend fun searchPostWithServiceBy(query: String):List<Post>
}