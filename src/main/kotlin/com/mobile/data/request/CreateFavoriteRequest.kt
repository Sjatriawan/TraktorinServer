package com.mobile.data.request


data class CreateFavoriteRequest(
    val postId:String,
    val userId:String,
    val price: Double,
    val fullname:String,
    val village:String
    )