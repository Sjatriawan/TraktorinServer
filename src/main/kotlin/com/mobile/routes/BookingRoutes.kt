package com.mobile.routes

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.request.BookingRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.BookingService
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.QueryParams
import com.mobile.util.ifEmailBelongsToUser
import com.mobile.util.ifEmailNotBelongToUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.doBooking(
    bookingService: BookingService,
    userService:UserService,

){
    authenticate {
        post("api/booking/create"){
            val request = call.receiveOrNull<BookingRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            ifEmailNotBelongToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
               when(bookingService.booking(request)){
                   is BookingService.ValidationEvents.ErrorFieldEmpty -> {
                       call.respond(
                           HttpStatusCode.OK,
                           BasicApiResponse(
                               successful = false,
                               message = ApiResponseMessages.FIELDS_BLANK
                           )
                       )
                   }
                   is BookingService.ValidationEvents.ErrorLength -> {
                       call.respond(
                           HttpStatusCode.BadRequest,
                           BasicApiResponse(
                               successful = false,
                               message = ApiResponseMessages.TOO_LONG
                           )
                       )
                   }
                   is BookingService.ValidationEvents.Success -> {
                       call.respond(
                           HttpStatusCode.OK,
                           BasicApiResponse(
                               successful = true
                           )
                       )
                   }
               }
            }
        }
    }
}
