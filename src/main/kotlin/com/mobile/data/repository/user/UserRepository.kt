package com.mobile.data.repository.user

import com.mobile.data.models.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id:String):User?

    suspend fun getUserByEmail(email:String):User?

    suspend fun doesPasswordForUserMatch(password:String , enteredPassword:String):Boolean
}