package com.mobile.data.repository.user

import com.mobile.data.models.User
import com.mobile.data.request.UpdateProfileRequest

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id:String):User?

    suspend fun getUserByEmail(email:String):User?

    suspend fun updateProfile(userId: String,profileImgUrl:String ,updateProfileRequest: UpdateProfileRequest):Boolean

    suspend fun doesPasswordForUserMatch(email:String , enteredPassword:String):Boolean

    suspend fun doesEmailBelongToUserId(email: String, userId:String):Boolean
}