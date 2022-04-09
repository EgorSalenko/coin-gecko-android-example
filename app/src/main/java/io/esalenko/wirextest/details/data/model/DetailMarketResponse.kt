package io.esalenko.wirextest.details.data.model

@kotlinx.serialization.Serializable
data class DetailMarketResponse(
    val id: String,
    val image: ImageResponse,
    val description: Description,
    val name: String
)

@kotlinx.serialization.Serializable
data class ImageResponse(
    val thumb: String,
    val small: String,
    val large: String
)

@kotlinx.serialization.Serializable
data class Description(val en: String)
