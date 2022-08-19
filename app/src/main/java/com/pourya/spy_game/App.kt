package com.pourya.spy_game

import android.app.Application
import com.pourya.spy_game.config.di.appModule
import com.pourya.spy_game.config.di.retrofitModule
import com.pourya.spy_game.config.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.environmentProperties

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger(Level.DEBUG)

            environmentProperties()

            //inject Android context
            androidContext(this@App)

            modules(listOf(retrofitModule, viewModelModule, appModule))
        }

    }
}