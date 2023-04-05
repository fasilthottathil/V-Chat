package com.vchat.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vchat.R
import com.vchat.ui.theme.Primary
import kotlinx.coroutines.delay

/**
 * Created by Fasil on 11/03/23.
 */

@Composable
fun Splash(onFinishDelay: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1300)
        onFinishDelay()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun SplashPreview() {
    Splash {}
}