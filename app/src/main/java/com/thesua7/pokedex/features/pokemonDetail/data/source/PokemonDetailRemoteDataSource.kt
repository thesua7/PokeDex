package com.thesua7.pokedex.features.pokemonDetail.data.source

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.safeApiCall
import com.thesua7.pokedex.core.network.ApiServiceInterface
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.PokemonResponse
import javax.inject.Inject

class PokemonDetailRemoteDataSource @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface
) {
    suspend fun gekPokemonDetail(name: String): ResultWrapper<PokemonResponse> {
        return safeApiCall {
            apiServiceInterface.getPokemon(name)
        }
    }
}
