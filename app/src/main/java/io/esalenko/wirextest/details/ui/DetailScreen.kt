package io.esalenko.wirextest.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var showSnackBar by remember { mutableStateOf(false) }

    val isRefreshing = detail == DetailsViewModel.ViewState.Loading

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            viewModel.refresh()
            showSnackBar = true
        },
        refreshTriggerDistance = 120.dp
    ) {
        when (detail) {
            is DetailsViewModel.ViewState.Success -> {
                SnackbarScreen(
                    snackbarMessage = "${detail.item.name} Updated Successfully",
                    showSnackbar = showSnackBar,
                ) {
                    Details(detail)
                }
            }
            is DetailsViewModel.ViewState.Error -> ErrorDialog()
        }
    }
}

@Composable
fun SnackbarScreen(
    snackbarMessage: String,
    showSnackbar: Boolean,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = showSnackbar, block = {
        scope.launch {
            if (showSnackbar) snackbarHostState.showSnackbar(snackbarMessage)
        }
    })
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}

@Composable
private fun Details(detail: DetailsViewModel.ViewState.Success) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                modifier = Modifier.padding(all = 32.dp),
                model = detail.item.image.large,
                contentDescription = null
            )
        }
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
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
}

@Composable
fun ErrorDialog(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = null,
            tint = Color.Red
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "Oops something went wrong!",
            style = MaterialTheme.typography.subtitle1,
            color = Color.Red
        )
    }
}
