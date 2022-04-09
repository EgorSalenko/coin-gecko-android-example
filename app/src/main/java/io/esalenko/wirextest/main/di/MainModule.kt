package io.esalenko.wirextest.main.di

import io.esalenko.wirextest.main.ui.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(androidApplication()) }
}
