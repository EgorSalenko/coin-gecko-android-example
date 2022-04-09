package io.esalenko.wirextest.main.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MarketResponse(
    val id: String,
    val name: String,
    val image: String,
    @SerialName("current_price") val currentPrice: Float,
)

fun MarketResponse.toEntity(): MarketEntity {
    return MarketEntity(id, name, image, currentPrice)
}
