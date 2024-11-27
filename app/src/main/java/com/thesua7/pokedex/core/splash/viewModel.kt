package com.thesua7.pokedex.core.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesua7.pokedex.core.data.local.CorePrefDataSource
import com.thesua7.pokedex.core.splash.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefDataSource: CorePrefDataSource
) : ViewModel() {

    var splashState by mutableStateOf<SplashState>(SplashState.Loading)
        private set

    fun checkAuthStatus() {
        viewModelScope.launch {
//            splashState = if (prefDataSource.isLoggedIn()) {
//                SplashState.NavigateToHome
//            } else {
//                SplashState.NavigateToAuth
//            }
            splashState = SplashState.NavigateToHome
        }
    }
}