package io.esalenko.wirextest.main.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.esalenko.wirextest.destinations.CurrencyDetailsDestination
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun Currencies(
    viewModel: MainViewModel = getViewModel(),
    destinationsNavigator: DestinationsNavigator
) {
    Button(onClick = {
        destinationsNavigator.navigate(CurrencyDetailsDestination())
    }) {
        Text(text = "Click")
    }
}
