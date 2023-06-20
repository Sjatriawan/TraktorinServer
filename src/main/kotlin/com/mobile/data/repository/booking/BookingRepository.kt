package com.mobile.data.repository.booking

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.response.FavoriteResponse
import com.mobile.response.OrderResponse

interface BookingRepository {

    suspend fun bookingPost(booking: Booking)

    suspend fun cancelBooking(bookingId:String)

    suspend fun getBookingForPost(postId: String):List<Booking>

    suspend fun getBooking(bookingId: String):Booking?


    suspend fun getOrderForUser(
        userId:String,
        page:Int,
        pageSize:Int
    ):List<OrderResponse>

}