package io.esalenko.wirextest.main.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.esalenko.wirextest.main.data.db.MarketDao
import io.esalenko.wirextest.main.data.model.MarketEntity
import io.esalenko.wirextest.main.data.source.MarketDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
internal class MarketRepositoryImpl @Inject constructor(
    mediator: MarketDataSource,
    private val dao: MarketDao
) : MarketRepository {

    private val config = PagingConfig(pageSize = 5, prefetchDistance = 5, enablePlaceholders = true)

    override val markets: Flow<PagingData<MarketEntity>> = Pager(
        config = config,
        remoteMediator = mediator
    ) { dao.pagingSource() }.flow

}