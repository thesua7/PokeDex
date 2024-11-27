package com.thesua7.pokedex.features.data.source.remote

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.safeApiCall
import com.thesua7.pokedex.core.network.ApiServiceInterface
import com.thesua7.pokedex.features.data.model.response.pokemon.PokemonResponse
import com.thesua7.pokedex.features.data.model.response.pokemonList.PokemonListResponse

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface
) {
    suspend fun getPokemonList(limit: Int, offset: Int): ResultWrapper<PokemonListResponse> {
        return safeApiCall {
            apiServiceInterface.getPokemonList(limit, offset)
        }
    }

    suspend fun gekPokemon(name: String): ResultWrapper<PokemonResponse> {
        return safeApiCall {
            apiServiceInterface.getPokemon(name)
        }
    }
}