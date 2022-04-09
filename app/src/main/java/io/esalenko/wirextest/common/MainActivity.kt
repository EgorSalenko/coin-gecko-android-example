package io.esalenko.wirextest.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import io.esalenko.wirextest.NavGraphs
import io.esalenko.wirextest.ui.theme.WirexTestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirexTestTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = rememberAnimatedNavController()
                )
            }
        }
    }
}
