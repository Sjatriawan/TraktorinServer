package com.mobile.services

import com.mobile.data.models.User
import com.mobile.data.repository.user.UserRepository
import com.mobile.data.request.CreateAccountRequest
import com.mobile.data.request.LoginRequest

class UserService(
    private val repository:UserRepository
){

    suspend fun doesUserWithEmailExist(email:String):Boolean{
        return repository.getUserByEmail(email) != null
    }

    suspend fun doesEmailBelongToUserId(email: String, userId:String):Boolean{
        return repository.doesEmailBelongToUserId(email,userId)
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
                username = request.username,
                password = request.password,
                address = "Pohgading",
                userPhone = 0,
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