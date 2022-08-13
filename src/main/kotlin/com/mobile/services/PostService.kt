package com.mobile.services

import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.CreatePostRequest
import com.mobile.response.PostResponse
import javax.swing.text.StyledEditorKit.BoldAction

class PostService(
    private val repository: PostRepository

){
    suspend fun createPostIfUserExist(request:CreatePostRequest):Boolean{
        return repository.createPostIfUserExist(
            Post(
                service_by = request.service_by,
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description,
                price = request.price.toDouble()
            )
        )
    }


    suspend fun getAllPost():List<Post>{
        return repository.getListPost()
    }

    suspend fun getPost(postId:String):Post ? = repository.getPost(postId)

    suspend fun deletePost(postId:String){
        return repository.deletePost(postId)
    }

    suspend fun searchPostByServiced(query:String):List<PostResponse>{
        val posts = repository.searchPostWithServiceBy(query)
        return posts.map { post ->
            PostResponse(
                service_by = post.service_by,
                description = post.description
            )
        }
    }
}