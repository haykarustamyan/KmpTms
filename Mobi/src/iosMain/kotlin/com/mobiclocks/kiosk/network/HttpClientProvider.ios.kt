package com.mobiclocks.kiosk.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import platform.Foundation.setValue

actual class HttpClientProvider actual constructor(private val accessToken: String) {
    actual fun create(): HttpClient {
        return HttpClient(Darwin) {
            engine {
                configureRequest {
                    setValue(accessToken, forHTTPHeaderField = "Access-Token")
                }
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























