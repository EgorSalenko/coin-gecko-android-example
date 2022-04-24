package io.esalenko.coingecko.details.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.esalenko.coingecko.details.data.repository.DetailsRepository
import io.esalenko.coingecko.details.data.repository.DetailsRepositoryImpl
import io.esalenko.coingecko.network.CoinApi

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {

    @Provides
    fun providesDetailsRepository(api: CoinApi): DetailsRepository = DetailsRepositoryImpl(api)

}
