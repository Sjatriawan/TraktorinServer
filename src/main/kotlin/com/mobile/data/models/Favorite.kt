package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Favorite(
    val postId:String,
    val userId:String,
    val price: Double = 0.0,
    val fullname:String,
    val village:String,
    @BsonId
    val id:String = ObjectId().toString()
)
