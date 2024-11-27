package com.thesua7.pokedex.features.pokemonDetail.domain.usecase

import com.thesua7.pokedex.core.base.useCase.BaseUseCase
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.features.pokemonDetail.domain.model.PokemonDetail
import com.thesua7.pokedex.features.pokemonDetail.domain.repository.PokemonDetailRepository
import com.thesua7.pokedex.features.pokemonList.domain.repository.PokemonListRepository
import javax.inject.Inject

class PokemonDetailUseCase @Inject constructor(private val repository: PokemonDetailRepository) :
    BaseUseCase<String, ResultWrapper<PokemonDetail>>(isInternetRequired = true) {
    override suspend fun execute(params: String): ResultWrapper<PokemonDetail> {
        return when (val result = repository.getPokemon(name = params)) {
            is ResultWrapper.Success -> {

                ResultWrapper.Success(
                    PokemonDetail(
                        sprites = result.value.sprites,
                        stats = result.value.stats,
                        types = result.value.types,
                        name = result.value.name,
                        id = result.value.id,
                        height = result.value.height,
                        weight = result.value.weight
                    )
                )
            }

            is ResultWrapper.Error -> ResultWrapper.Error(
                UiError.NetworkError(
                    "No Internet Connection.", 404
                )
            )

            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }
}