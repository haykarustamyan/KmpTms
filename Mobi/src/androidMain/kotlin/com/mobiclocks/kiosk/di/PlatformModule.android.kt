package com.mobiclocks.kiosk.di

import com.mobiclocks.kiosk.ClockViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual class PlatformModule {
    actual val module: Module = module {

        factory { ClockViewModel() }
    }
}