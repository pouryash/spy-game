package com.pourya.spy_game.config.di

import com.pourya.spy_game.repository.CategoryRepository
import com.pourya.spy_game.repository.WordRepository
import com.pourya.spy_game.util.SharedPreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        CategoryRepository()
    }

    single {
        WordRepository()
    }

    single { SharedPreferenceManager(androidContext()) }

}