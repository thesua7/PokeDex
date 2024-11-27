package com.thesua7.pokedex.features.pokemonList.data.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonList.data.model.dto.PokemonListResponse
import com.thesua7.pokedex.features.pokemonList.data.source.PokemonListRemoteDataSource
import com.thesua7.pokedex.features.pokemonList.domain.repository.PokemonListRepository
import javax.inject.Inject

class PokemonListDataRepository @Inject constructor(
    private val pokemonListRemoteDataSource: PokemonListRemoteDataSource,
) : PokemonListRepository {

    override suspend fun getPokemonList(
        limit: Int, offset: Int
    ): ResultWrapper<PokemonListResponse> {
        return pokemonListRemoteDataSource.getPokemonList(limit, offset)
    }

}