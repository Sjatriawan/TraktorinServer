package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Booking(
    val userId:String,
    val userServiceId:String,
    val postId: String,
    val are: Double,
    val imageUrl:String,
    val employee:String,
    val address:String,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
