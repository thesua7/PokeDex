package com.thesua7.pokedex.features.presentation.pokemonList.event

sealed class PokemonListEvent {
    data object LoadPokemonList : PokemonListEvent()
    data class ShowError(val message: String) : PokemonListEvent()
}
