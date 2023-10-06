package com.mobile.services

import com.mobile.data.models.Booking
import com.mobile.data.repository.booking.BookingRepository
import com.mobile.data.request.BookingRequest
import com.mobile.response.OrderResponse
import com.mobile.util.Constant

class BookingService(
    private val bookingRepository: BookingRepository
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
        val address = postService.getPost(bookingRequest.postId)?.village ?: ""
        val imageBooking = postService.getPost(bookingRequest.postId)?.imageUrl ?: ""
        bookingRepository.bookingPost(
            Booking(
                userId = bookingRequest.userId ,
                postId = bookingRequest.postId,
                timestamp = System.currentTimeMillis(),
                are = bookingRequest.are * getPrice as Double,
                employee = employee,
                userServiceId = userServiceId,
                address = address,
                imageUrl = imageBooking
            )
        )
        return ValidationEvents.Success
    }

    suspend fun getOrder(postId: String):Booking?{
        return bookingRepository.getBooking(postId)
    }

    suspend fun getOrderForUser(
        userId:String,
        page:Int = 0,
        pageSize:Int = Constant.DEFAULT_POST_PAGE_SIZE
    ):List<OrderResponse> {
        return bookingRepository.getOrderForUser(userId,page,pageSize)
    }

    sealed class ValidationEvents{
        object ErrorFieldEmpty:ValidationEvents()
        object Success:ValidationEvents()
        object ErrorLength:ValidationEvents()
    }
}

