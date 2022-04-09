package io.esalenko.wirextest.details.di

import io.esalenko.wirextest.details.ui.DetailsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel {
        DetailsViewModel(androidApplication())
    }
}
