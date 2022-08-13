package com.mobile.data.repository.booking

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.data.request.BookingRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class BookingRepositoryImpl(
    db:CoroutineDatabase
):BookingRepository {
    private val booking = db.getCollection<Booking>()
    private val users = db.getCollection<User>()
    private val posts = db.getCollection<Post>()

    val book = db.getCollection<Booking>()

    override suspend fun bookingPost(booking: Booking) {
        book.insertOne(booking)
    }

    override suspend fun deleteBooking(bookingId: String): Boolean {
        val deleteCount = booking.deleteOneById(bookingId).deletedCount
        return deleteCount > 0
    }

    override suspend fun getBookingForPost(postId: String): List<Booking> {
        return book.find(Booking::postId eq postId).toList()
    }

    override suspend fun getBooking(bookingId: String): Booking? {
        return book.findOneById(bookingId)
    }


}