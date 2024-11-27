package com.thesua7.pokedex.features.pokemonDetail.presentation.page

import com.thesua7.pokedex.core.base.viewModel.BaseViewModel
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonDetail.domain.usecase.PokemonDetailUseCase
import com.thesua7.pokedex.features.pokemonDetail.presentation.event.PokemonDetailEvent
import com.thesua7.pokedex.features.pokemonDetail.presentation.state.PokemonDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonDetailUseCase: PokemonDetailUseCase,
) : BaseViewModel<PokemonDetailState, PokemonDetailEvent>(PokemonDetailState()) {



    fun onEvent(event: PokemonDetailEvent) {
        when (event) {
            is PokemonDetailEvent.Success -> loadPokemonDetails(event.pokemonName)
            is PokemonDetailEvent.ShowError -> Timber.e("Error: ${event.message}")
        }
    }




    private fun loadPokemonDetails(pokemonName: String) {
        updateState { copy(isLoading = true) }

        executeWithHandling(task = {
            when (val result = pokemonDetailUseCase(pokemonName)) {
                is ResultWrapper.Success -> {
                    updateState { copy(isLoading = false, data = result.value, error = null) }
                }
                is ResultWrapper.Error -> {
                    updateState { copy(isLoading = false, error = result.error) }
                }
                ResultWrapper.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }, onError = { uiError ->
            updateState { copy(isLoading = false, error = uiError) }
        })
    }
}