package com.mobile.services

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.repository.booking.BookingRepository
import com.mobile.data.request.BookingRequest

class BookingService(
    private val bookingRepository: BookingRepository,
) {
    suspend fun cancelBooking(userId: String,bookId:String) {
        return bookingRepository.cancelBooking(bookId)
    }
    suspend fun booking(userId:String,bookingRequest: BookingRequest, postService: PostService):ValidationEvents{
        bookingRequest.apply {
            if (are.isNaN() || userId.isBlank() || postId.isBlank()){
                return ValidationEvents.ErrorFieldEmpty
            }
        }

        val getPrice = postService.getPost(bookingRequest.postId)?.price ?: ""
        val employee = postService.getPost(bookingRequest.postId)?.fullname ?:""
        val userServiceId = postService.getPost(bookingRequest.postId)?.userId ?: ""
        bookingRepository.bookingPost(
            Booking(
                userId = bookingRequest.userId ,
                postId = bookingRequest.postId,
                timestamp = System.currentTimeMillis(),
                are = bookingRequest.are * getPrice as Double,
                employee = employee,
                userServiceId = userServiceId
            )
        )
        return ValidationEvents.Success
    }

    sealed class ValidationEvents{
        object ErrorFieldEmpty:ValidationEvents()
        object Success:ValidationEvents()
        object ErrorLength:ValidationEvents()
    }
}

