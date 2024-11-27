package com.thesua7.pokedex.features.pokemonDetail.presentation.state

import com.thesua7.pokedex.core.base.baseState.BaseUiState
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.features.pokemonDetail.domain.model.PokemonDetail


data class PokemonDetailState(
    override var data: PokemonDetail? = null,
    override val error: UiError? = null,
    override val isLoading: Boolean = false,
) : BaseUiState<PokemonDetail>(data, error, isLoading) {

    override fun copyWith(
        data: PokemonDetail?,
        error: UiError?,
        isLoading: Boolean
    ): BaseUiState<PokemonDetail> {
        return PokemonDetailState(
            data = data ?: this.data,
            error = error ?: this.error,
            isLoading = isLoading,
        )
    }

}
