package com.thesua7.pokedex.features.data.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.data.model.response.pokemon.PokemonResponse
import com.thesua7.pokedex.features.data.model.response.pokemonList.PokemonListResponse
import com.thesua7.pokedex.features.data.source.remote.RemoteDataSource
import com.thesua7.pokedex.features.domain.repository.PokeDexRepository
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : PokeDexRepository {
    override suspend fun getPokemonList(
        limit: Int, offset: Int
    ): ResultWrapper<PokemonListResponse> {
        return remoteDataSource.getPokemonList(limit, offset)
    }

    override suspend fun getPokemon(name: String): ResultWrapper<PokemonResponse> {

        return remoteDataSource.gekPokemon(name)
    }
}