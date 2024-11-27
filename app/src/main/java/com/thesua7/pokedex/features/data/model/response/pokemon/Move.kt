package com.thesua7.pokedex.features.data.model.response.pokemon

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)