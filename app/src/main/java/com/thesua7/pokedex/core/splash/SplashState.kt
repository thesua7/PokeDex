package com.thesua7.pokedex.core.splash

sealed class SplashState {
    data object Loading : SplashState()
    data object NavigateToAuth : SplashState()
    data object NavigateToHome : SplashState()
}