package io.esalenko.wirextest.network

import io.esalenko.wirextest.details.data.model.DetailMarketResponse
import io.esalenko.wirextest.main.data.model.MarketResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): List<MarketResponse>

    @GET("coins/{id}")
    suspend fun getMarketData(@Path("id") id: String): DetailMarketResponse

}
