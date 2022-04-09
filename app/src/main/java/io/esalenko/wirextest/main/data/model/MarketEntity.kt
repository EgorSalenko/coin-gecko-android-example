package io.esalenko.wirextest.main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("market")
data class MarketEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val image: String,
    val currentPrice: Float
)
