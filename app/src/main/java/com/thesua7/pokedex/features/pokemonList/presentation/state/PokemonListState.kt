package com.thesua7.pokedex.features.pokemonList.presentation.state

import com.thesua7.pokedex.core.base.baseState.BaseUiState
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.features.pokemonList.domain.entity.Pokemon


data class PokemonListState(
    override var data: List<Pokemon> = emptyList(),
    override val error: UiError? = null,
    override val isLoading: Boolean = false,
) : BaseUiState<List<Pokemon>>(data, error, isLoading) {

    override fun copyWith(
        data: List<Pokemon>?, error: UiError?, isLoading: Boolean
    ): PokemonListState {
        return PokemonListState(
            data = data ?: this.data,
            error = error ?: this.error,
            isLoading = isLoading,
        )
    }

}
