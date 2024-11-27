package com.thesua7.pokedex.features.data.model.response.pokemonList

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)