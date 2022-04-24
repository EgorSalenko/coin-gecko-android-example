package io.esalenko.coingecko.details.data.repository

import io.esalenko.coingecko.details.data.model.DetailMarketResponse
import io.esalenko.coingecko.network.CoinApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val api: CoinApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DetailsRepository {

    override suspend fun getDetailedMarket(id: String): DetailMarketResponse =
        withContext(ioDispatcher) { api.getMarketData(id) }
}
