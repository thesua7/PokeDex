package com.thesua7.pokedex.features.domain.useCase

import com.thesua7.pokedex.core.base.useCase.BaseUseCase
import com.thesua7.pokedex.core.exceptions.ResultWrapper
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.features.domain.model.Pokemon
import com.thesua7.pokedex.features.domain.repository.PokeDexRepository
import java.util.Locale
import javax.inject.Inject

class PokemonListUseCase @Inject constructor(
    private val pokeDexRepository: PokeDexRepository
) : BaseUseCase<PokemonListUseCase.Param, ResultWrapper<List<Pokemon>>>(isInternetRequired = true) {

    override suspend fun execute(params: Param): ResultWrapper<List<Pokemon>> {
        // Proceed with the API call
        val result = pokeDexRepository.getPokemonList(params.limit, params.offset)

        return when (result) {
            is ResultWrapper.Success -> {
                val pokemonList = result.value.results.mapIndexed { _, entry ->
                    val number = entry.url.trimEnd('/').substringAfterLast('/').toInt()
                    val url =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                    Pokemon(
                        pokemonId = number,
                        pokemonName = entry.name.capitalize(Locale.ROOT),
                        imageUrl = url
                    )
                }
                ResultWrapper.Success(pokemonList)
            }

            is ResultWrapper.Error -> ResultWrapper.Error(
                UiError.NetworkError(
                    "No Internet Connection.", 404
                )
            )

            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }

    data class Param(val limit: Int, val offset: Int)
}
