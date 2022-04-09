package io.esalenko.wirextest.main.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.esalenko.wirextest.db.AppDatabase
import io.esalenko.wirextest.main.data.model.MarketEntity
import io.esalenko.wirextest.main.data.model.MarketResponse
import io.esalenko.wirextest.main.data.model.toEntity
import io.esalenko.wirextest.network.CoinGeckoApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalPagingApi
internal class MarketDataSource @Inject constructor(
    private val api: CoinGeckoApi,
    private val db: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteMediator<Int, MarketEntity>() {

    companion object {
        private const val FIRST_PAGE = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketEntity>
    ): MediatorResult =
        withContext(ioDispatcher) {
            try {
                val nextPage = when (loadType) {
                    LoadType.REFRESH -> FIRST_PAGE
                    LoadType.APPEND -> state.anchorPosition?.plus(1) ?: FIRST_PAGE
                    else -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }

                val marketResponses = api.getMarkets(page = nextPage)

                db.runInTransaction {
                    if (loadType == LoadType.REFRESH) db.marketDao().clearDb()

                    marketResponses
                        .map(MarketResponse::toEntity)
                        .let(db.marketDao()::insertMarkets)
                }

                return@withContext MediatorResult.Success(endOfPaginationReached = marketResponses.isEmpty())
            } catch (e: Exception) {
                return@withContext MediatorResult.Error(e)
            }
        }

}
