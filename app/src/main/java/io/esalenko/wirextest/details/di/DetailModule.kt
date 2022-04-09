package io.esalenko.wirextest.details.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.esalenko.wirextest.details.data.repository.DetailsRepository
import io.esalenko.wirextest.details.data.repository.DetailsRepositoryImpl
import io.esalenko.wirextest.network.CoinApi

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {

    @Provides
    fun providesDetailsRepository(api: CoinApi): DetailsRepository = DetailsRepositoryImpl(api)

}
