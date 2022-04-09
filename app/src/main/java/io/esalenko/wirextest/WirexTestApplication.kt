package io.esalenko.wirextest

import android.app.Application
import io.esalenko.wirextest.common.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WirexTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WirexTestApplication.applicationContext)
            modules(appModules)
        }
    }
}
