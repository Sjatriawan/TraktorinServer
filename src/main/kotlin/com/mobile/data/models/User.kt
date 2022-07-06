package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username:String,
    val password:String,
    val address:String,
    val userPhone:Long,
    val email:String,
    val profileImgUrl:String,
    val lat:Double,
    val lng:Double,
    val isService:Boolean = false,
    @BsonId
    val id:String = ObjectId().toString(),
)
