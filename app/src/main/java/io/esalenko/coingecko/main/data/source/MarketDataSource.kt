package io.esalenko.coingecko.main.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.esalenko.coingecko.db.AppDatabase
import io.esalenko.coingecko.db.RemoteKeysEntity
import io.esalenko.coingecko.main.data.model.MarketEntity
import io.esalenko.coingecko.main.data.model.MarketResponse
import io.esalenko.coingecko.main.data.model.toEntity
import io.esalenko.coingecko.network.CoinApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalPagingApi
internal class MarketDataSource @Inject constructor(
    private val api: CoinApi,
    private val db: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteMediator<Int, MarketEntity>() {

    companion object {
        private const val FIRST_PAGE = 1
        private const val REMOTE_KEY_ID = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketEntity>
    ): MediatorResult =
        withContext(ioDispatcher) {
            try {
                val nextPage: Int = when (loadType) {
                    LoadType.REFRESH -> FIRST_PAGE
                    LoadType.APPEND -> {
                        db.remoteKeysDao().getRemoteKeys(REMOTE_KEY_ID).nextKey
                    }
                    LoadType.PREPEND -> {
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    }
                }

                val marketResponses = api.getMarkets(page = nextPage)

                db.runInTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.marketDao().clear()
                        db.remoteKeysDao().clear()
                    }

                    db.remoteKeysDao()
                        .insertRemoteKey(RemoteKeysEntity(REMOTE_KEY_ID, nextPage + 1))

                    marketResponses
                        .map(MarketResponse::toEntity)
                        .let(db.marketDao()::insertAll)
                }

                return@withContext MediatorResult.Success(endOfPaginationReached = marketResponses.isEmpty())
            } catch (e: Exception) {
                return@withContext MediatorResult.Error(e)
            }
        }

}
