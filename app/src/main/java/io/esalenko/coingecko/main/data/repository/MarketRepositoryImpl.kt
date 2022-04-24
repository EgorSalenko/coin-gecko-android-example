package io.esalenko.coingecko.main.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.esalenko.coingecko.main.data.db.MarketDao
import io.esalenko.coingecko.main.data.model.MarketEntity
import io.esalenko.coingecko.main.data.source.MarketDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
internal class MarketRepositoryImpl @Inject constructor(
    mediator: MarketDataSource,
    private val dao: MarketDao
) : MarketRepository {

    private val config = PagingConfig(
        pageSize = PAGE_SIZE,
        prefetchDistance = (PAGE_SIZE + PREFETCH_THRESHOLD),
        enablePlaceholders = false
    )

    override val markets: Flow<PagingData<MarketEntity>> = Pager(
        config = config,
        remoteMediator = mediator
    ) { dao.pagingSource() }.flow

    companion object {
        private const val PAGE_SIZE = 10
        private const val PREFETCH_THRESHOLD = 5
    }
}
