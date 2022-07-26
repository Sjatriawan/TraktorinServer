package com.mobile.services

import com.mobile.data.models.Post
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.CreatePostRequest

class PostService(
    private val repository: PostRepository

){
    suspend fun createPostIfUserExist(request:CreatePostRequest):Boolean{
        return repository.createPostIfUserExist(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

}