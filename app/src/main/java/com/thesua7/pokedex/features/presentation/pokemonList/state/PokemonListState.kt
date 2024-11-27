package com.thesua7.pokedex.features.presentation.pokemonList.state

import com.thesua7.pokedex.core.base.baseState.BaseUiState
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.features.domain.model.Pokemon


data class PokemonListState(
    override val data: List<Pokemon> = emptyList(),
    override val error: UiError? = null,
    override val isLoading: Boolean = false,


    ) : BaseUiState<List<Pokemon>>(data, error, isLoading) {

    override fun copyWith(
        data: List<Pokemon>?, error: UiError?, isLoading: Boolean
    ): PokemonListState {
        return PokemonListState(data ?: emptyList(), error ?: this.error, isLoading)
    }
}