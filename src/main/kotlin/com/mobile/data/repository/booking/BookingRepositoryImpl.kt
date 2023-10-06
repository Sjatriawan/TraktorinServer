package com.mobile.data.repository.booking

import com.mobile.data.models.Booking
import com.mobile.data.models.Post
import com.mobile.data.models.User
import com.mobile.response.OrderResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class BookingRepositoryImpl(
    db:CoroutineDatabase
):BookingRepository {
    private val book = db.getCollection<Booking>()
    private val users = db.getCollection<User>()


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

    override suspend fun getOrderForUser(userId: String, page: Int, pageSize: Int): List<OrderResponse> {
        return book.find(Booking::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map { order ->
                val user = users.findOneById(order.userId)
                OrderResponse(
                    userId = userId,
                    id = order.id,
                    employee = order.employee,
                    are = order.are,
                    imgUrl = order.imageUrl,
                    address = order.address,
                )
            }
    }
}
