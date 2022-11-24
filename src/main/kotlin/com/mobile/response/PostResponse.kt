package com.mobile.response

data class PostResponse(
    val id:String,
    val userId:String,
    val imageUrl:String,
    val fullname:String,
    val description:String,
    val isOwnPost:Boolean,
    val price:Double,
    val village:String,
    val district:String,
    val province:String,
)
