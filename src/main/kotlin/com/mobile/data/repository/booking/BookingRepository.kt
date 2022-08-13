package com.mobile.data.repository.booking

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.request.BookingRequest

interface BookingRepository {

    suspend fun bookingPost(booking: Booking)

    suspend fun deleteBooking(bookingId:String):Boolean

    suspend fun getBookingForPost(postIdId: String):List<Booking>

    suspend fun getBooking(bookingId: String):Booking?
}