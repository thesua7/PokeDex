package com.thesua7.pokedex.features.domain.useCase

import com.thesua7.pokedex.core.base.useCase.BaseUseCase
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.features.data.model.response.pokemon.PokemonResponse
import com.thesua7.pokedex.features.domain.repository.PokeDexRepository
import javax.inject.Inject

class PokemonUseCase @Inject constructor(private  val repository: PokeDexRepository) :BaseUseCase<String,ResultWrapper<PokemonResponse>>() {
    override suspend fun execute(params: String): ResultWrapper<PokemonResponse> {
        return repository.getPokemon(params)
    }
}