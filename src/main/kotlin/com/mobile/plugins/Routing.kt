package com.mobile.plugins

import com.mobile.routes.*
import com.mobile.services.BookingService
import com.mobile.services.FavoriteService
import com.mobile.services.PostService
import com.mobile.services.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService:UserService by inject()
    val postService:PostService by inject()
    val favoriteService:FavoriteService by inject()
    val bookingService:BookingService by inject()
    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        //Create account
        createUserRoutes(userService)
        //Auth
        authenticate()
        //Login
        loginUser(userService = userService, jwtIssuer = jwtIssuer, jwtAudience = jwtAudience, jwtSecret = jwtSecret)

        //Update profile
        updateUserRoutes(userService)
        //Create post
        createPostRoutes(postService)
        //GetPost
        getListPostRoute(postService)
        //Delete post
        deletePostRoute(postService,userService)
        //Search Post
        searchPost(postService)
        //Add Favorite
        addFavoriteRoute(favoriteService,userService)
        //Delete Favorite
        deleteFavoriteRoute(favoriteService,userService)
        //Booking
        doBooking(bookingService,userService, postService)
        //getBooking
        cancelBooking(userService,bookingService)

        static("/static"){
            resources("static")
        }



    }
}
