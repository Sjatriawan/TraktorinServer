package com.mobile.plugins

import com.mobile.data.repository.post.PostRepository
import com.mobile.data.repository.user.UserRepository
import com.mobile.routes.createPostRoute
import com.mobile.routes.createUserRoutes
import com.mobile.routes.loginUser
import com.mobile.services.PostService
import com.mobile.services.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService:UserService by inject()
    val postService:PostService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    routing {
        //Create account
        createUserRoutes(userService)

        //Login
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        //CreatePost
        createPostRoute(postService, userService)
    }
}
