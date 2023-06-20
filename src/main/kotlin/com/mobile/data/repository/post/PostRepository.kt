package com.mobile.data.repository.post

import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.response.PostResponse
import com.mobile.util.Constant

interface PostRepository {

    suspend fun createPostIfUserExist(post: Post):Boolean

    suspend fun deletePost(postId:String)

    suspend fun getPostForHome(
        userId:String,
        page:Int,
        pageSize:Int
    ):List<PostResponse>

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPostForProfile(
        ownUserId:String,
        userId:String,
        page:Int,
        pageSize:Int
    ):List<PostResponse>


    suspend fun getPost(postId: String):Post?


    suspend fun searchPostWithServiceBy(query: String):List<Post>
}