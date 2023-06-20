package com.mobile.di

import com.google.gson.Gson
import com.mobile.data.models.Favorite
import com.mobile.data.repository.booking.BookingRepository
import com.mobile.data.repository.booking.BookingRepositoryImpl
import com.mobile.data.repository.favorite.FavoriteRepository
import com.mobile.data.repository.favorite.FavoriteRepositoryImpl
import com.mobile.data.repository.post.PostRepository
import com.mobile.data.repository.post.PostRepositoryImpl
import com.mobile.data.repository.user.UserRepository
import com.mobile.data.repository.user.UserRepositoryImpl
import com.mobile.services.BookingService
import com.mobile.services.FavoriteService
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.Constant
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.math.sin


val mainModule = module {
    single {
         val client = KMongo.createClient().coroutine
         client.getDatabase(Constant.DATABASE_NAME)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<PostRepository> {
        PostRepositoryImpl(get())
    }

    single<FavoriteRepository>{
        FavoriteRepositoryImpl(get())
    }
    single<BookingRepository>{
        BookingRepositoryImpl(get())
    }

    single {
        FavoriteService(get(), get())
    }

    single{
        UserService(get())
    }
    single{
        PostService(get())
    }

    single {
        BookingService(get())
    }

    single {
        Gson()
    }
    //Anjing

}