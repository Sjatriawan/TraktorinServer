package com.mobile.services

import com.mobile.data.models.Post
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.CreatePostRequest
import com.mobile.response.PostResponse

class PostService(
    private val repository: PostRepository

){
//    suspend fun createPostIfUserExist(request:CreatePostRequest, userId:String, imgUrl:String):Boolean{
//        return repository.createPostIfUserExist(
//            Post(
//                service_by = request.service_by,
//                imageUrl = imgUrl,
//                userId = userId,
//                timestamp = System.currentTimeMillis(),
//                description = request.description,
//                price = request.price.toDouble(),
//                village = request.village,
//                district = request.district,
//                province = request.province
//            )
//        )
//    }

    suspend fun createPostIfUserExist(request: CreatePostRequest, userId: String, imageUrl: String): Boolean {
        return repository.createPostIfUserExist(
            Post(
                service_by = request.service_by,
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description,
                price = request.price.toDouble(),
                village = request.village,
                district = request.district,
                province = request.province
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