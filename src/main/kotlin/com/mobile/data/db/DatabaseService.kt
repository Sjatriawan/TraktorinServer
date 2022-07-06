package com.mobile.data.db

import com.mobile.data.models.User

interface DatabaseService {

    suspend fun createUser(user: User)

    suspend fun getUserById(id:String):User
}