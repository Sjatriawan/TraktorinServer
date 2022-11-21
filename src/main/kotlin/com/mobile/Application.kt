package com.mobile

import com.mobile.di.mainModule
import io.ktor.server.application.*
import com.mobile.plugins.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.nio.file.Paths

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)
@Suppress("unused")
fun Application.module() {
    install(Koin){
        modules(mainModule)
    }
    configureRouting()
    //configureSockets()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()

    println(Paths.get("").toAbsolutePath().toString())



}
