package com.thesua7.pokedex.features.di

import com.thesua7.pokedex.features.domain.repository.PokeDexRepository
import com.thesua7.pokedex.features.domain.useCase.PokemonListUseCase
import com.thesua7.pokedex.features.domain.useCase.PokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object PokeDexModule {


    @Provides
    fun providePokemonListUseCase(pokeDexRepository: PokeDexRepository):PokemonListUseCase{
        return PokemonListUseCase(pokeDexRepository)
    }

    @Provides
    fun providePokemonUseCase(pokeDexRepository: PokeDexRepository):PokemonUseCase{
        return PokemonUseCase(pokeDexRepository)
    }
}