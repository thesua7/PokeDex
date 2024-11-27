package com.thesua7.pokedex.features.pokemonDetail.data.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.PokemonResponse
import com.thesua7.pokedex.features.pokemonDetail.data.source.PokemonDetailRemoteDataSource
import com.thesua7.pokedex.features.pokemonDetail.domain.repository.PokemonDetailRepository
import javax.inject.Inject



class PokemonDetailDataRepository @Inject constructor(
    private val pokemonDetailRemoteDataSource: PokemonDetailRemoteDataSource,
) : PokemonDetailRepository {
    override suspend fun getPokemon(name: String): ResultWrapper<PokemonResponse> {
        return pokemonDetailRemoteDataSource.gekPokemonDetail(name)
    }

}