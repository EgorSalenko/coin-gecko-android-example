package io.esalenko.wirextest.details.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.esalenko.wirextest.destinations.DetailsScreenDestination
import io.esalenko.wirextest.details.data.model.DetailMarketResponse
import io.esalenko.wirextest.details.data.repository.DetailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    saveState: SavedStateHandle,
    app: Application,
    private val repository: DetailsRepository
) : AndroidViewModel(app) {

    private val detailsScreenNavArgs = DetailsScreenDestination.argsFrom(saveState)

    private val _detailFlow = MutableStateFlow<ViewState>(ViewState.Loading)
    val detailFlow = _detailFlow

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _detailFlow.value = ViewState.Loading
            kotlin.runCatching {
                val details = repository.getDetailedMarket(detailsScreenNavArgs.id)
                _detailFlow.value = ViewState.Success(details)
            }.onFailure {
                _detailFlow.value = ViewState.Error
            }
        }
    }

    sealed class ViewState {
        data class Success(val item: DetailMarketResponse) : ViewState()
        object Loading : ViewState()
        object Error : ViewState()
    }
}
