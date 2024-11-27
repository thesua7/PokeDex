package com.thesua7.pokedex.features.pokemonList.data.source

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.safeApiCall
import com.thesua7.pokedex.core.network.ApiServiceInterface
import com.thesua7.pokedex.features.pokemonList.data.model.dto.PokemonListResponse

import javax.inject.Inject

class PokemonListRemoteDataSource @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface
) {
    suspend fun getPokemonList(limit: Int, offset: Int): ResultWrapper<PokemonListResponse> {
        return safeApiCall {
            apiServiceInterface.getPokemonList(limit, offset)
        }
    }

}