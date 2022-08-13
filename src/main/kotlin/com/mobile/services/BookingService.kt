package com.mobile.services

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.repository.booking.BookingRepository
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.request.BookingRequest

class BookingService(
    private val bookingRepository: BookingRepository,
    private val postRepository: PostRepository

) {
    suspend fun getPost(postId:String):Post ? = postRepository.getPost(postId)

    suspend fun booking(bookingRequest: BookingRequest):ValidationEvents{
        bookingRequest.apply {
            if (are.isNaN() || userId.isBlank() || postId.isBlank()){
                return ValidationEvents.ErrorFieldEmpty
            }
        }


        bookingRepository.bookingPost(
            Booking(
                userId = bookingRequest.userId ,
                postId = bookingRequest.postId,
                timestamp = System.currentTimeMillis(),
                are = bookingRequest.are * (getPost(postId = bookingRequest.postId)!!.price),
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