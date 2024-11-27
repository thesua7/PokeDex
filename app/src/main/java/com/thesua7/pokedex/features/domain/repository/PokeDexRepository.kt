package com.thesua7.pokedex.features.domain.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.data.model.response.pokemon.PokemonResponse
import com.thesua7.pokedex.features.data.model.response.pokemonList.PokemonListResponse

interface PokeDexRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): ResultWrapper<PokemonListResponse>
    suspend fun getPokemon(name:String):ResultWrapper<PokemonResponse>
}