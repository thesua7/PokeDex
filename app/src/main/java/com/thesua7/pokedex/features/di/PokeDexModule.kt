package com.thesua7.pokedex.features.di

import com.thesua7.pokedex.features.pokemonDetail.domain.repository.PokemonDetailRepository
import com.thesua7.pokedex.features.pokemonList.domain.repository.PokemonListRepository
import com.thesua7.pokedex.features.pokemonList.domain.usecase.PokemonListUseCase
import com.thesua7.pokedex.features.pokemonDetail.domain.usecase.PokemonDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object PokeDexModule {


    @Provides
    fun providePokemonListUseCase(pokemonListRepository: PokemonListRepository): PokemonListUseCase {
        return PokemonListUseCase(pokemonListRepository)
    }

    @Provides
    fun providePokemonDetailUseCase(pokemonDetailsRepository: PokemonDetailRepository): PokemonDetailUseCase {
        return PokemonDetailUseCase(pokemonDetailsRepository)
    }


}