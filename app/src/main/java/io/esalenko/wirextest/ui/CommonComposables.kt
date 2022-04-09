package io.esalenko.wirextest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.esalenko.wirextest.R
import kotlinx.coroutines.launch


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
            text = stringResource(id = R.string.common_error),
            style = MaterialTheme.typography.subtitle1,
            color = Color.Red
        )
    }
}
