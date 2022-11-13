package com.example.dela

import android.app.Application
import com.example.dela.data.di.dataModule
import com.example.dela.di.appModule
import com.example.dela.ui.di.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DelaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DelaApplication)
            modules(appModule, dataModule, uiModules)
        }
    }
}