package com.mobiclocks.kiosk.network

import io.ktor.client.HttpClient

import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

actual class HttpClientProvider actual constructor(private val accessToken: String) {
    actual fun create(): HttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Access-Token", accessToken)
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        return HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(accessToken, accessToken)
                    }
                }
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }
}