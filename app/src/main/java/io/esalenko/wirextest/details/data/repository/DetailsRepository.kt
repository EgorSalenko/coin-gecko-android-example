package io.esalenko.wirextest.details.data.repository

import io.esalenko.wirextest.details.data.model.DetailMarketResponse

interface DetailsRepository {
    suspend fun getDetailedMarket(id: String): DetailMarketResponse
}
