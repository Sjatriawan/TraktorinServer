package com.mobile.data.repository.user

import com.mobile.data.models.User
import com.mobile.data.request.UpdateProfileRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class UserRepositoryImpl(
    db:CoroutineDatabase
): UserRepository {
    private  val users = db.getCollection<User>()
    override suspend fun createUser(user: User) {
       users.insertOne(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun updateProfile(
        userId: String,
        profileImgUrl:String,
        updateProfileRequest: UpdateProfileRequest)
    : Boolean {
        val user = getUserById(userId) ?: return false
        return users.updateOneById(
            id= userId,
            update = User(
                email = user.email,
                username = user.username,
                userPhone = updateProfileRequest.userPhone,
                password = user.password,
                village = updateProfileRequest.village,
                district = updateProfileRequest.district,
                province = updateProfileRequest.province,
                fullname = updateProfileRequest.fullname,
                profileImgUrl = profileImgUrl,
                lat = user.lat,
                lng = user.lng,
                id = user.id,
            )
        ).wasAcknowledged()
    }

    override suspend fun doesPasswordForUserMatch(
        email: String,
        enteredPassword: String)
    : Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        return users.findOneById(userId)?.email == email
    }

}