package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Booking(
    val userId:String,
    val postId: String,
    val are: Double,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
