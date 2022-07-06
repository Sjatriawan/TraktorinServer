package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post (
    val userId:String,
    val imageUrl:String,
    val merkTraktor:String,
    val timestamp:Long,
    val description:String,
    @BsonId
    val id:String = ObjectId().toString()
    )