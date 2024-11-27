package com.thesua7.pokedex.features.utils

import androidx.compose.ui.graphics.Color
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.Stat
import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.Type
import com.thesua7.pokedex.ui.theme.AtkColor
import com.thesua7.pokedex.ui.theme.DefColor
import com.thesua7.pokedex.ui.theme.HPColor
import com.thesua7.pokedex.ui.theme.SpAtkColor
import com.thesua7.pokedex.ui.theme.SpDefColor
import com.thesua7.pokedex.ui.theme.SpdColor
import com.thesua7.pokedex.ui.theme.TypeBug
import com.thesua7.pokedex.ui.theme.TypeDark
import com.thesua7.pokedex.ui.theme.TypeDragon
import com.thesua7.pokedex.ui.theme.TypeElectric
import com.thesua7.pokedex.ui.theme.TypeFairy
import com.thesua7.pokedex.ui.theme.TypeFighting
import com.thesua7.pokedex.ui.theme.TypeFire
import com.thesua7.pokedex.ui.theme.TypeFlying
import com.thesua7.pokedex.ui.theme.TypeGhost
import com.thesua7.pokedex.ui.theme.TypeGrass
import com.thesua7.pokedex.ui.theme.TypeGround
import com.thesua7.pokedex.ui.theme.TypeIce
import com.thesua7.pokedex.ui.theme.TypeNormal
import com.thesua7.pokedex.ui.theme.TypePoison
import com.thesua7.pokedex.ui.theme.TypePsychic
import com.thesua7.pokedex.ui.theme.TypeRock
import com.thesua7.pokedex.ui.theme.TypeSteel
import com.thesua7.pokedex.ui.theme.TypeWater
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.toLowerCase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}