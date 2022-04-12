package io.esalenko.wirextest.main.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MarketResponse(
    val id: String,
    val name: String,
    val image: String,
    @SerialName("current_price") val currentPrice: Float,
    @SerialName("price_change_24h") val priceChange: Float,
    @SerialName("market_cap") val marketCap: Long
)

fun MarketResponse.toEntity(): MarketEntity = MarketEntity(
    id = id,
    name = name,
    image = image,
    currentPrice = currentPrice,
    priceChange = priceChange,
    marketCap = marketCap
)
