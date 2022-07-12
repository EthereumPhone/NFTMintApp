package org.ethereumphone.nftcreator

import android.app.Application
import org.ethereumphone.nftcreator.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // stat koin
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}