package io.esalenko.wirextest.main.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.esalenko.wirextest.destinations.CurrencyDetailsDestination
import io.esalenko.wirextest.main.data.model.MarketEntity

@Destination(start = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val listMarkets = viewModel.marketsFlow.collectAsLazyPagingItems()

    LazyColumn(Modifier.fillMaxSize()) {
        items(listMarkets) { item ->
            item?.let {
                MarketItem(item) {
                    navigator.navigate(CurrencyDetailsDestination(id = item.id))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MarketItem(item: MarketEntity, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),

        onClick = onClick
    ) {
        Row(
            Modifier
                .padding(all = 8.dp)
                .fillMaxHeight()
        ) {
            CurrencyImage(item)
            CurrencyShortDescription(item)
        }
    }
}

@Composable
private fun CurrencyShortDescription(item: MarketEntity) {
    Column(
        Modifier
            .padding(start = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = item.name, style = MaterialTheme.typography.subtitle1)
        Text(text = item.currentPrice.toString(), style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun CurrencyImage(item: MarketEntity) {

    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
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

    SubcomposeAsyncImage(
        modifier = Modifier.size(50.dp),
        imageLoader = imageLoader,
        model = item.image,
        loading = { CircularProgressIndicator() },
        contentDescription = null
    )
}
