package com.thesua7.pokedex.features.pokemonDetail.domain.model

import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.Sprites
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.Stat
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.Type

data class PokemonDetail (
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val name: String,
    val id: Int,
    val height: Int,
    val weight: Int,
)