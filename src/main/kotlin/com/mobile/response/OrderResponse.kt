package com.mobile.response

data class OrderResponse(
    val id: String,
    val userId: String,
    val employee: String,
    val are: Double,
    val address: String,
    val imgUrl: String
)