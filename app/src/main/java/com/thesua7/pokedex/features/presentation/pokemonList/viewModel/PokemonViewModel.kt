package com.thesua7.pokedex.features.presentation.pokemonList.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thesua7.pokedex.core.base.viewModel.BaseViewModel
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.core.resources.NetworkInfo
import com.thesua7.pokedex.features.domain.useCase.PokemonListUseCase
import com.thesua7.pokedex.features.presentation.pokemonList.event.PokemonListEvent
import com.thesua7.pokedex.features.presentation.pokemonList.state.PokemonListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonListUseCase: PokemonListUseCase,
    private val networkUtils: NetworkInfo // Inject the NetworkInfo for connectivity checks
) : BaseViewModel<PokemonListState, PokemonListEvent>(PokemonListState()) {

    private var currPage = 0
    var endReached = mutableStateOf(false)
    private var isLoadingMore = false

    init {
        onEvent(PokemonListEvent.LoadPokemonList)

        // Observe internet connection changes
        observeInternetConnectivity()
    }

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.LoadPokemonList -> loadPokemonList()
            is PokemonListEvent.ShowError -> Timber.e("Error: ${event.message}")
        }
    }

    private fun loadPokemonList() {
        if (endReached.value || isLoadingMore) return

        isLoadingMore = true
        val isFirstLoad = currPage == 0

        if (isFirstLoad) {
            updateState { copy(isLoading = true) }
        }

        executeWithHandling(task = {
            val result = pokemonListUseCase(PokemonListUseCase.Param(limit = 20, offset = currPage * 20))

            when (result) {
                is ResultWrapper.Success -> {
                    val newPokemonList = result.value
                    updateState {
                        copy(
                            data = data + newPokemonList,
                            isLoading = false,
                            error = null
                        )
                    }
                    if (newPokemonList.size < 20) {
                        endReached.value = true
                    } else {
                        currPage++
                    }
                }
                is ResultWrapper.Error -> {
                    updateState { copy(isLoading = false, error = result.error) }
                }
                ResultWrapper.Loading -> {
                    if (isFirstLoad) {
                        updateState { copy(isLoading = true) }
                    }
                }
            }
            isLoadingMore = false
        }, onError = { uiError ->
            updateState { copy(isLoading = false, error = uiError) }
            isLoadingMore = false
        })
    }

    // Observe the internet connectivity changes and reload data when restored
    private fun observeInternetConnectivity() {
        viewModelScope.launch {
            networkUtils.observeInternetConnectivity()
                .collect { isConnected ->
                    if (isConnected) {
                        Timber.d("Internet restored. Reloading data...")
                        loadPokemonList() // Retry loading data when the internet is restored
                    }
                }
        }
    }

    fun refreshData() {
        resetState()
        currPage = 0
        endReached.value = false
        loadPokemonList()
    }
}
