package com.mobile.services

import com.mobile.data.models.User
import com.mobile.data.repository.user.UserRepository
import com.mobile.data.request.CreateAccountRequest
import com.mobile.data.request.LoginRequest
import com.mobile.data.request.UpdateProfileRequest
import com.mobile.response.ProfileResponse

class UserService(
    private val repository:UserRepository
){

    suspend fun doesUserWithEmailExist(email:String):Boolean{
        return repository.getUserByEmail(email) != null
    }

    suspend fun doesEmailBelongToUserId(email: String, userId:String):Boolean{
        return repository.doesEmailBelongToUserId(email,userId)
    }

    suspend fun getUserByEmail(email: String):User?{
        return repository.getUserByEmail(email)
    }

    suspend fun getUserProfile(userId: String, callerUserId:String):ProfileResponse? {
        val user = repository.getUserById(userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            username = user.username,
            fullname = user.fullname,
            village = user.village,
            district = user.district,
            province = user.province,
            profileImgUrl = user.profileImgUrl,
            userPhone = user.userPhone,
            isOwnProfile = userId == callerUserId
        )
    }

    suspend fun updateProfile(
        userId : String,
        profileImgUrl: String,
        updateProfileRequest: UpdateProfileRequest
    ):Boolean{
        return repository.updateProfile(userId, profileImgUrl, updateProfileRequest)
    }

    suspend fun doesPasswordMatchForUser(request: LoginRequest):Boolean{
        return repository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
        )
    }

    suspend fun createUser(request: CreateAccountRequest){
        repository.createUser(
            User(
                email = request.email,
                fullname= "",
                username = request.username,
                password = request.password,
                village = "",
                district = "",
                province = "",
                userPhone = 62,
                profileImgUrl = "",
                lat = 0.0,
                lng = 0.0,
                isService = false,
            )
        )
    }

    fun validateCreateAccount(request: CreateAccountRequest):ValidationEvent{
        if(request.email.isBlank() || request.username.isBlank() || request.password.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent{
        object ErrorFieldEmpty:ValidationEvent()
        object Success:ValidationEvent()
    }

}