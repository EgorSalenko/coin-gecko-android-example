package io.esalenko.wirextest.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import io.esalenko.wirextest.NavGraphs
import io.esalenko.wirextest.ui.theme.WirexTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirexTestTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
