package com.mobile.data.request

data class CreatePostRequest (
    val username:String,
    val fullname:String,
    val description:String,
    val price:String,
    val village:String,
    val district:String,
    val province:String
    )

