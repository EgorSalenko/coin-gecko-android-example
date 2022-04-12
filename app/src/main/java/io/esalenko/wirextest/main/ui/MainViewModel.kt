package io.esalenko.wirextest.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.esalenko.wirextest.main.data.repository.MarketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app: Application,
    repository: MarketRepository
) : AndroidViewModel(app) {

    val autoRefreshTick = flow {
        while (true) {
            delay(AUTOUPDATE_TIMEOUT)
            emit(Unit)
        }
    }.flowOn(Dispatchers.Default).conflate()

    val marketsFlow = repository
        .markets
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    companion object {
        private const val AUTOUPDATE_TIMEOUT = 30_000L
    }
}
