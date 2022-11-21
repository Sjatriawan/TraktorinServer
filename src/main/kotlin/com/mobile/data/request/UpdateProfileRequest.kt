package com.mobile.data.request

data class UpdateProfileRequest(
    val username:String,
    val fullname:String,
    val village:String,
    val district:String,
    val province:String,
    val userPhone:Long,
    val email:String,
    val profileImgUrl:String,
    val profileImgChanged:Boolean = false,
    val lat:Double,
    val lng:Double,
)