package io.esalenko.wirextest.main.data.source

import android.util.Log
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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
internal class MarketDataSource @Inject constructor(
    private val api: CoinGeckoApi,
    private val db: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteMediator<Int, MarketEntity>() {

    companion object {
        const val TAG = "MarketDataSource"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarketEntity>
    ): MediatorResult =
        withContext(ioDispatcher) {
            try {
                val nextPage = when (loadType) {
                    LoadType.REFRESH -> {
                        Log.d(TAG, "load REFRESH: ${state.anchorPosition}")
                        1
                    }
                    LoadType.APPEND -> {
                        Log.d(TAG, "load APPEND: ${state.anchorPosition}")
                        state.anchorPosition?.plus(1) ?: 1
                    }
                    else -> {
                        return@withContext MediatorResult.Success(endOfPaginationReached = true)
                    }
                }

                val markets = api.getMarkets(page = nextPage)

                db.runInTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.marketDao().clearDb()
                    }
                    markets
                        .map(MarketResponse::toEntity)
                        .let(db.marketDao()::insertMarkets)
                }

                return@withContext MediatorResult.Success(endOfPaginationReached = markets.isEmpty())
            } catch (e: IOException) {
                return@withContext MediatorResult.Error(e)
            } catch (e: HttpException) {
                return@withContext MediatorResult.Error(e)
            }
        }
}
