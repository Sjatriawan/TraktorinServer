package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Booking(
    val name:String,
    val timestamp:Long,
    val methodPay:Int,
    val lat:Double,
    val lng:Double,
    val price:Double,
    @BsonId
    val id: String = ObjectId().toString()
)
