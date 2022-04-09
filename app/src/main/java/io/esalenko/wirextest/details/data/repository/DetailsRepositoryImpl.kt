package io.esalenko.wirextest.details.data.repository

import io.esalenko.wirextest.details.data.model.DetailMarketResponse
import io.esalenko.wirextest.network.CoinGeckoApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DetailsRepository {

    override suspend fun getDetailedMarket(id: String): DetailMarketResponse =
        withContext(ioDispatcher) { api.getMarketData(id) }
}
