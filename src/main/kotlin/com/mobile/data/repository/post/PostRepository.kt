package com.mobile.data.repository.post

import com.mobile.data.models.Post

interface PostRepository {

    suspend fun createPostIfUserExist(post: Post):Boolean

    suspend fun deletePost(postId:String)

    suspend fun getListPost():List<Post>

    suspend fun getPost(postId: String):Post?
}