package io.esalenko.coingecko.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideMarketDao(database: AppDatabase) = database.marketDao()

    @Provides
    fun provideRemoteKeysDao(database: AppDatabase) = database.remoteKeysDao()
}
