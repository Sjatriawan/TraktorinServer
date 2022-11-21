package com.mobile.data.request

data class CreatePostRequest (
    val service_by:String,
    val description:String,
    val price:String,
    val village:String,
    val district:String,
    val province:String
    )

