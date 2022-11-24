package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post (
    val fullname:String,
    val userId:String,
    val village:String,
    val district:String,
    val province:String,
    val imageUrl:String,
    val timestamp:Long,
    val description:String,
    val price:Double,
    @BsonId
    val id:String = ObjectId().toString()
    )