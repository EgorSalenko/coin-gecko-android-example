package io.esalenko.wirextest.main.di

import androidx.paging.ExperimentalPagingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.esalenko.wirextest.db.AppDatabase
import io.esalenko.wirextest.main.data.repository.MarketRepository
import io.esalenko.wirextest.main.data.repository.MarketRepositoryImpl
import io.esalenko.wirextest.main.data.source.MarketDataSource
import io.esalenko.wirextest.network.CoinApi

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    internal fun provideMarketDataSource(api: CoinApi, db: AppDatabase): MarketDataSource =
        MarketDataSource(api, db)

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    internal fun provideMarketRepository(
        mediator: MarketDataSource,
        db: AppDatabase
    ): MarketRepository = MarketRepositoryImpl(mediator, db.marketDao())
}
