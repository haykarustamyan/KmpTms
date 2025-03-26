package com.mobiclocks.kiosk

import androidx.compose.ui.window.ComposeUIViewController
import com.mobiclocks.kiosk.di.PlatformModule
import com.mobiclocks.kiosk.di.commonModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    startKoin {
        modules(PlatformModule().module + commonModule)
    }
    App()
}