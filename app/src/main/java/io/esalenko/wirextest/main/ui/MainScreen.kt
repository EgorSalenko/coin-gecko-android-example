package io.esalenko.wirextest.main.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.esalenko.wirextest.R
import io.esalenko.wirextest.destinations.DetailsScreenDestination
import io.esalenko.wirextest.main.data.model.MarketEntity
import io.esalenko.wirextest.ui.SnackbarScreen
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val listMarkets = viewModel.marketsFlow.collectAsLazyPagingItems()

    val isError = listMarkets.loadState.refresh is LoadState.Error

    LaunchedEffect(listMarkets.loadState) {
        launch {
            viewModel.autoRefreshTick.collect {
                listMarkets.refresh()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            elevation = 8.dp,
            title = {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = stringResource(id = R.string.app_name)
                )
            }
        )
        SnackbarScreen(
            snackbarMessage = stringResource(id = R.string.common_error),
            showSnackbar = isError
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(listMarkets) { item ->
                    item?.let {
                        MarketItem(item = item) {
                            navigator.navigate(DetailsScreenDestination(id = item.id))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarketItem(
    modifier: Modifier = Modifier,
    item: MarketEntity,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxHeight()
        ) {
            CurrencyImage(image = item.image)
            CurrencyShortDescription(item = item)
        }
    }
}

@Composable
private fun CurrencyShortDescription(
    modifier: Modifier = Modifier,
    item: MarketEntity
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Black
        )
        Text(
            text = stringResource(id = R.string.currency, item.currentPrice),
            style = MaterialTheme.typography.caption
        )
        Row {
            Text(
                text = stringResource(id = R.string.percentage_change),
                style = MaterialTheme.typography.caption
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = String.format("%.2f", item.priceChange),
                color = if (item.priceChange < 0.0f) Color.Red else Color.Green,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
private fun CurrencyImage(
    modifier: Modifier = Modifier,
    image: String
) {

    val context = LocalContext.current
    val imageLoader = cachableImageLoader(context)

    SubcomposeAsyncImage(
        modifier = modifier.size(50.dp),
        imageLoader = imageLoader,
        model = image,
        loading = { CircularProgressIndicator(modifier = Modifier.size(10.dp)) },
        contentDescription = null
    )
}

private fun cachableImageLoader(context: Context) = ImageLoader.Builder(context)
    .memoryCache {
        MemoryCache.Builder(context)
            .maxSizePercent(0.25)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
    .build()
