package com.mobile.routes

import com.mobile.data.models.Booking
import com.mobile.data.repository.booking.BookingRepository
import com.mobile.data.request.BookingRequest
import com.mobile.data.request.CancelBookingRequest
import com.mobile.response.BasicApiResponse
import com.mobile.services.BookingService
import com.mobile.services.FavoriteService
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.ApiResponseMessages
import com.mobile.util.QueryParams
import com.mobile.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.awt.print.Book

fun Route.doBooking(
    bookingService: BookingService,
    userService:UserService,
    postService: PostService

){
    authenticate {
        post("api/booking/create"){
            val request = call.receiveOrNull<BookingRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userId = call.userId
            when(bookingService.booking(userId,request, postService)){
                is BookingService.ValidationEvents.ErrorFieldEmpty -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }
                is BookingService.ValidationEvents.ErrorLength -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.TOO_LONG
                        )
                    )
                }
                is BookingService.ValidationEvents.Success -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                }
            }
        }
    }
}

fun Route.getListOrderRoute(
    bookingService: BookingService
){
    authenticate {
        get("api/order/get"){
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: 0
            val order = bookingService.getOrderForUser(call.userId,page,pageSize)

            call.respond(HttpStatusCode.OK, order)
        }
    }
}

fun Route.cancelBooking(
    userService: UserService,
    bookingService: BookingService
){
    authenticate {
        delete ("api/booking/cancel") {
            val cancel = call.receiveOrNull<CancelBookingRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val userId = call.userId
            val booking = bookingService.cancelBooking(userId,cancel.userId)
            if (booking == null){
                call.respond(HttpStatusCode.NotFound,)
                return@delete
            }else{
                bookingService.cancelBooking(userId,bookId = cancel.bookingId)
                call.respond(HttpStatusCode.OK)
            }


        }
    }
}

fun Route.getOrderDetail(
    bookingService:BookingService
){
    authenticate{
        get("api/order/detail") {
            val orderId = call.parameters["order_id"] ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest)
                return@get
            }

            val order = bookingService.getOrder(orderId) ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound)
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = order
                )
            )
        }
    }
}