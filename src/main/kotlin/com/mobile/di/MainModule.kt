package com.mobile.di

import com.mobile.data.repository.post.PostRepository
import com.mobile.data.repository.post.PostRepositoryImpl
import com.mobile.data.repository.user.UserRepository
import com.mobile.data.repository.user.UserRepositoryImpl
import com.mobile.services.PostService
import com.mobile.services.UserService
import com.mobile.util.Constant
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo



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

    single{
        UserService(get())
    }
    single{
        PostService(get())
    }

}