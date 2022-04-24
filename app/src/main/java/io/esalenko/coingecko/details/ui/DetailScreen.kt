package io.esalenko.coingecko.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.esalenko.coingecko.R
import io.esalenko.coingecko.ui.ErrorDialog
import io.esalenko.coingecko.ui.SnackbarScreen

@Destination
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    id: String,
    navigator: DestinationsNavigator
) {

    val detail = viewModel.detailFlow.collectAsState().value

    var showSnackBar by remember { mutableStateOf(false) }

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    isRefreshing = detail == DetailsViewModel.ViewState.Loading

    Column {
        TopAppBar(
            elevation = 8.dp,
            title = {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = stringResource(id = R.string.app_name)
                )
            },
            navigationIcon = {
                IconButton(onClick = navigator::popBackStack) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = {
                viewModel.refresh()
                showSnackBar = true
            }
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
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Black
            )
        }
        item {

            val description = HtmlCompat.fromHtml(
                detail.item.description.en,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString().ifEmpty { stringResource(id = R.string.empty_description) }

            Text(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                text = description,
                style = MaterialTheme.typography.body2,
                fontSize = 18.sp
            )
        }
    }
}
