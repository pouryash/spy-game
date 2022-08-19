package com.pourya.spy_game.config.di

import android.content.Context
import com.pourya.spy_game.viewmodel.CategoryViewModel
import com.pourya.spy_game.viewmodel.WordViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (context: Context) -> CategoryViewModel(context) }
    viewModel { (context: Context) -> WordViewModel(context) }

}