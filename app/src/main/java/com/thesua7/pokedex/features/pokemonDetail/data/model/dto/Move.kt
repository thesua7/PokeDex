package com.thesua7.pokedex.features.pokemonDetail.data.model.dto

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)