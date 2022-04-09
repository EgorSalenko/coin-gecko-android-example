package io.esalenko.wirextest.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    id: String
) {

    val detail = viewModel.detailFlow.collectAsState().value

    val isRefreshing = detail == DetailsViewModel.ViewState.Loading
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = viewModel::refresh,
        refreshTriggerDistance = 120.dp
    ) {
        when (detail) {
            is DetailsViewModel.ViewState.Success -> {
                Details(detail, isRefreshing)
            }
            is DetailsViewModel.ViewState.Error -> {
                ErrorDialog()
            }
            else -> {}
        }
    }

}

@Composable
private fun Details(
    detail: DetailsViewModel.ViewState.Success,
    isRefreshing: Boolean
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .padding(all = 32.dp)
                        .align(Alignment.Center),
                    model = detail.item.image.large,
                    contentDescription = null
                )
            }
            item {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp),
                    text = detail.item.name,
                    style = MaterialTheme.typography.h5
                )
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    text = detail.item.description.en,
                    style = MaterialTheme.typography.body2
                )
            }

        }
        LaunchedEffect(key1 = isRefreshing, block = {
            scope.launch {
                snackbarHostState.showSnackbar("${detail.item.name} Updated Successfully")
            }
        })
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}

@Composable
fun ErrorDialog() {

}
