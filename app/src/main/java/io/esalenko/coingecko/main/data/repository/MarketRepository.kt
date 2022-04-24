package io.esalenko.coingecko.main.data.repository

import androidx.paging.PagingData
import io.esalenko.coingecko.main.data.model.MarketEntity
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    val markets: Flow<PagingData<MarketEntity>>
}
