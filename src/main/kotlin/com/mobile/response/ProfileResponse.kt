package com.mobile.response

data class ProfileResponse(
    val username:String,
    val fullname:String,
    val village:String,
    val district:String,
    val province:String,
    val userPhone:Long,
    val profileImgUrl:String,
    val isOwnProfile:Boolean
)