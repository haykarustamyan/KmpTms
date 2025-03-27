package com.mobiclocks.kiosk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiclocks.kiosk.network.HttpClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClockViewModel(
    private val httpClient: HttpClient = HttpClientProvider(accessToken = "af98517c-f0dc-4334-82cb-639e0839424a").create()
) : ViewModel() {
    private val _clockState = MutableStateFlow(ClockUiState(isLoading = false))
    val clockState: StateFlow<ClockUiState> = _clockState

    fun fetchClockData() {
        _clockState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response: String =
                    httpClient.get("https://dev.mobiclocks.com/api/clock-app/clock").bodyAsText()
                _clockState.update { it.copy(isLoading = false, data = response) }
            } catch (e: Exception) {
                _clockState.update { it.copy(isLoading = false, data = "Error: $e") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close()
    }
}