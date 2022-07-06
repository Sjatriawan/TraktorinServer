package com.mobile

import com.mobile.di.mainModule
import io.ktor.server.application.*
import com.mobile.plugins.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)



@Suppress("unused")
fun Application.module() {
    configureRouting()
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    install(Koin){
        modules(mainModule)
    }

}
