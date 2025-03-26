package com.mobiclocks.kiosk.network

import io.ktor.client.*

expect class HttpClientProvider(accessToken: String) {
    fun create(): HttpClient
}
