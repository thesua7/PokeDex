package com.thesua7.pokedex.features.data.model.response.pokemon

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)