package com.thesua7.pokedex.features.pokemonDetail.domain.repository

import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.PokemonResponse

interface PokemonDetailRepository {
    suspend fun getPokemon(name:String): ResultWrapper<PokemonResponse>

}