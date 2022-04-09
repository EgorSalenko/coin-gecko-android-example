package io.esalenko.wirextest.main.data.repository

import androidx.paging.PagingData
import io.esalenko.wirextest.main.data.model.MarketEntity
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    val markets: Flow<PagingData<MarketEntity>>
}
