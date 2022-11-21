package com.mobile.data.repository.booking

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.data.request.BookingRequest
import com.mongodb.client.result.DeleteResult
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class BookingRepositoryImpl(
    db:CoroutineDatabase
):BookingRepository {
    private val book = db.getCollection<Booking>()
    private val users = db.getCollection<User>()
    private val posts = db.getCollection<Post>()


    override suspend fun bookingPost(booking: Booking) {
        book.insertOne(booking)
    }

    override suspend fun cancelBooking(bookingId: String) {
          book.deleteOneById(bookingId)
    }

    override suspend fun getBookingForPost(postId: String): List<Booking> {
        return book.find(Booking::postId eq postId).toList()
    }

    override suspend fun getBooking(bookingId: String): Booking? {
        return book.findOneById(bookingId)
    }


}