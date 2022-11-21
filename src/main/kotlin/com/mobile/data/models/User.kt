package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username:String,
    val fullname:String,
    val password:String,
    val village:String,
    val district:String,
    val province:String,
    val userPhone:Long,
    val postCount: Int = 0,
    val email:String,
    val profileImgUrl:String,
    val lat:Double,
    val lng:Double,
    val isService:Boolean = false,
    @BsonId
    val id:String = ObjectId().toString(),
)
