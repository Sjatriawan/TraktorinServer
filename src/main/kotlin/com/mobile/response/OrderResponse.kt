package com.mobile.response

data class OrderResponse(
    val id: String,
    val userId: String,
    val fullname: String,
    val price: Double,
    val village: String,
    val imgOrder: String
)