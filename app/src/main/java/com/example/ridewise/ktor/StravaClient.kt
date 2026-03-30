package com.example.ridewise.ktor

import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val stravaClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    install(Logging) {
        logger = Logger.SIMPLE      // prints to Logcat
        level = LogLevel.ALL        // logs request/response headers and body
    }
}