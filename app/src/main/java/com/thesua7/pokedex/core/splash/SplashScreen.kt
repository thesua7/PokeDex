// SplashScreen.kt
package com.thesua7.pokedex.core.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToAuth: (() -> Unit)?,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash_animation.json"))

    // Start authentication check after delay
    LaunchedEffect(Unit) {
        delay(3000) // Wait for splash animation
        viewModel.checkAuthStatus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition, modifier = Modifier.size(200.dp)
        )
    }

    // Observe navigation state and navigate when ready
    LaunchedEffect(viewModel.splashState) {
        when (viewModel.splashState) {
            is SplashState.NavigateToAuth -> if (onNavigateToAuth != null) {
                onNavigateToAuth()
            }
            is SplashState.NavigateToHome -> onNavigateToHome()
            SplashState.Loading -> { /* No-op */
            }
        }
    }
}


// SplashViewModel.kt remains the same as previous implementation
// SplashState.kt remains the same as previous implementation