package io.esalenko.coingecko.main.ui

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.esalenko.coingecko.R
import io.esalenko.coingecko.destinations.DetailsScreenDestination
import io.esalenko.coingecko.main.data.model.MarketEntity
import io.esalenko.coingecko.ui.SnackbarScreen
import kotlinx.coroutines.launch

const val FIRST_ITEM_INDEX = 0

@Destination(start = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val pagingSource = viewModel.marketsFlow.collectAsLazyPagingItems()
    val lazyState = rememberLazyListState()

    val isError = remember { mutableStateOf(false) }
    isError.value = pagingSource.loadState.refresh is LoadState.Error

    LaunchedEffect(pagingSource.loadState) {
        launch {
            viewModel.autoRefreshTick.collect {
                if (lazyState.firstVisibleItemIndex == FIRST_ITEM_INDEX) {
                    pagingSource.refresh()
                }
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
            showSnackbar = isError.value
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyState) {
                items(key = { it.id }, items = pagingSource) { item ->
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
    val textSizeLarge = 18.sp
    val textSizeSmall = 16.sp
    Column(
        modifier = modifier
            .padding(start = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Black,
            fontSize = textSizeLarge
        )
        Text(
            text = stringResource(id = R.string.currency, item.currentPrice),
            style = MaterialTheme.typography.caption,
            fontSize = textSizeSmall
        )
        Row {
            Text(
                text = stringResource(id = R.string.percentage_change),
                style = MaterialTheme.typography.caption,
                fontSize = textSizeSmall
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = String.format("%.2f", item.priceChange),
                color = if (item.priceChange < 0.0f) Color.Red else Color.Green,
                style = MaterialTheme.typography.caption,
                fontSize = textSizeSmall
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
    val imageLoader = remember {
        cachableImageLoader(context)
    }

    AsyncImage(
        modifier = modifier.size(90.dp),
        imageLoader = imageLoader,
        model = image,
        contentDescription = null
    )
}

private fun cachableImageLoader(context: Context) = ImageLoader.Builder(context)
    .memoryCache {
        MemoryCache.Builder(context)
            .maxSizePercent(0.25)
            .weakReferencesEnabled(true)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
    .build()
