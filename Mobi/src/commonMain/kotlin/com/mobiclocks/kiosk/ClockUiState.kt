package com.mobiclocks.kiosk

data class ClockUiState(
    val isLoading: Boolean = false,
    val data: String? = null,
    val errorMessage: String? = null
)