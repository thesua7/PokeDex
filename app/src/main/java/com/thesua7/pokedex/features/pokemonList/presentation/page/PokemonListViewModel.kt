package com.thesua7.pokedex.features.pokemonList.presentation.page

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thesua7.pokedex.core.base.viewModel.BaseViewModel
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonList.domain.entity.Pokemon
import com.thesua7.pokedex.features.pokemonList.domain.usecase.PokemonListUseCase
import com.thesua7.pokedex.features.pokemonList.presentation.event.PokemonListEvent
import com.thesua7.pokedex.features.pokemonList.presentation.state.PokemonListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonListUseCase: PokemonListUseCase,
) : BaseViewModel<PokemonListState, PokemonListEvent>(PokemonListState()) {

    private var currPage = 0
    var endReached = mutableStateOf(false)
    private var isLoadingMore = false

    private var cachedPokemonList = emptyList<Pokemon>()
    private var isSearchingStarted = true
    var isSearching = mutableStateOf(false)

    init {
        onEvent(PokemonListEvent.LoadPokemonList)

    }


    fun searchPokemonList(query: String) {
        val currentList = if (isSearchingStarted) {
            uiState.value.data
        } else {
            cachedPokemonList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                isSearching.value = false
                isSearchingStarted = true
                updateState { copy(data = cachedPokemonList) }
                return@launch
            }

            val results = currentList.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.pokemonId.toString() == query.trim()
            }

            if (isSearchingStarted) {
                cachedPokemonList = uiState.value.data
                isSearchingStarted = false
            }

            updateState { copy(data = results) }
            isSearching.value = true
        }
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


    fun refreshData() {
        resetState()
        currPage = 0
        endReached.value = false
        loadPokemonList()
    }
}
