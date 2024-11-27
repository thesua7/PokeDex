package com.thesua7.pokedex.features.pokemonList.domain.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonList.data.model.dto.PokemonListResponse

interface PokemonListRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): ResultWrapper<PokemonListResponse>
}