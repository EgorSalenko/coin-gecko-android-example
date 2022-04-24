package io.esalenko.coingecko.details.data.repository

import io.esalenko.coingecko.details.data.model.DetailMarketResponse

interface DetailsRepository {
    suspend fun getDetailedMarket(id: String): DetailMarketResponse
}
