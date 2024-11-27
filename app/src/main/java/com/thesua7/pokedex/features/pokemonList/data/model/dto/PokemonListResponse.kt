package com.thesua7.pokedex.features.pokemonList.data.model.dto

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)