package com.mobiclocks.kiosk

import android.app.Application
import com.mobiclocks.kiosk.di.PlatformModule
import com.mobiclocks.kiosk.di.commonModule
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(PlatformModule().module + commonModule)
        }
    }
}