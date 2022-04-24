package io.esalenko.coingecko.details.data.model

import androidx.annotation.Keep

@Keep
@kotlinx.serialization.Serializable
data class DetailMarketResponse(
    val id: String,
    val image: ImageResponse,
    val description: Description,
    val name: String
)

@Keep
@kotlinx.serialization.Serializable
data class ImageResponse(
    val thumb: String,
    val small: String,
    val large: String
)

@Keep
@kotlinx.serialization.Serializable
data class Description(val en: String)
