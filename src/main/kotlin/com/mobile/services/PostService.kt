package com.mobile.services

import com.mobile.data.models.Post
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.CreatePostRequest
import com.mobile.response.PostResponse
import com.mobile.util.Constant

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
                fullname = request.fullname,
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description,
                price = request.price,
                village = request.village,
                district = request.district,
                province = request.province,
            )
        )
    }


//    suspend fun getAllPost():List<Post>{
//        return repository.getListPost()
//    }

    suspend fun getPostForHome(
        userId:String,
        page:Int = 0,
        pageSize:Int = Constant.DEFAULT_POST_PAGE_SIZE
    ):List<PostResponse> {
        return repository.getPostForHome(userId,page,pageSize)
    }

    suspend fun deletePost(postId:String){
        return repository.deletePost(postId)
    }

    suspend fun getPost(
        postId: String
    ):Post?{
        return repository.getPost(postId)
    }

    suspend fun searchPostByServiced(query:String):List<PostResponse>{
        val posts = repository.searchPostWithServiceBy(query)
        return posts.map { post ->
            PostResponse(
                id = post.id,
                userId = post.userId,
                imageUrl = post.imageUrl,
                fullname = post.fullname,
                description = post.description,
                isOwnPost = true,
                price = post.price,
                village = post.village,
                district = post.district,
                province = post.province,
            )
        }
    }

//    suspend fun getPostForProfile(
//        userId: String,
//        page: Int = 0,
//        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
//    ):List<PostResponse> {
//        return repository.getPostForProfile(userId,page,pageSize)
//    }
}