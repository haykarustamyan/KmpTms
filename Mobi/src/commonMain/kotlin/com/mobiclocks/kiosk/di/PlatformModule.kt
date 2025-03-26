package com.mobiclocks.kiosk.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect class PlatformModule {
    val module: Module
}

val commonModule = module {
//    includes(userModule, loginModule)
}