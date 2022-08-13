package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post (
    val service_by:String,
    val userId:String,
    val imageUrl:String,
    val timestamp:Long,
    val description:String,
    val price:Double,
    @BsonId
    val id:String = ObjectId().toString()
    )