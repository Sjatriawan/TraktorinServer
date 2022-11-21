package com.mobile.util

import com.mobile.plugins.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


val ApplicationCall.userId:String
    get() = principal<JWTPrincipal>()?.userId.toString()