package com.mobile.plugins

import com.mobile.data.repository.user.UserRepository
import com.mobile.routes.createUserRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoutes(userRepository)
    }
}
