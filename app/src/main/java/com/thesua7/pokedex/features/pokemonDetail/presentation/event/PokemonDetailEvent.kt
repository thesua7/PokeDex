package com.thesua7.pokedex.features.pokemonDetail.presentation.event

sealed class PokemonDetailEvent {

    data class ShowError(val message: String): PokemonDetailEvent()
    data class Success(val pokemonName: String): PokemonDetailEvent()

}