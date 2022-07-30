package com.mobile.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Favorite(
    val userId:String,
    val parentId:String,
    @BsonId
    val id:String = ObjectId().toString()
)
