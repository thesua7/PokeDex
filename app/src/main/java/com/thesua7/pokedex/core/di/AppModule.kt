package com.thesua7.pokedex.core.di

import android.content.Context
import com.thesua7.pokedex.core.data.PreferencesRepository
import com.thesua7.pokedex.core.data.local.CorePrefDataSource
import com.thesua7.pokedex.core.data.local.EncryptedPrefUtil
import com.thesua7.pokedex.core.network.ApiInterceptor
import com.thesua7.pokedex.core.network.ApiServiceInterface
import com.thesua7.pokedex.core.resources.NetworkInfo
import com.thesua7.pokedex.core.resources.SnackBarManager
import com.thesua7.pokedex.features.pokemonDetail.data.repository.PokemonDetailDataRepository
import com.thesua7.pokedex.features.pokemonDetail.data.source.PokemonDetailRemoteDataSource
import com.thesua7.pokedex.features.pokemonDetail.domain.repository.PokemonDetailRepository
import com.thesua7.pokedex.features.pokemonList.data.repository.PokemonListDataRepository
import com.thesua7.pokedex.features.pokemonList.data.source.PokemonListRemoteDataSource
import com.thesua7.pokedex.features.pokemonList.domain.repository.PokemonListRepository
import com.thesua7.pokedex.features.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton // Singleton ensures one instance throughout the app lifecycle
    fun provideOkHttpClient(apiInterceptor: ApiInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(apiInterceptor)  // Attach the TokenInterceptor to add the token to API requests
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY  // Log request and response bodies for debugging
            }).build()
    }

    @Provides
    @Singleton // // Creates and provides a Retrofit instance also add the token to API requests
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)  // Base URL for all API requests
            .client(okHttpClient)  // Pass the OkHttpClient with token interception to Retrofit
            .addConverterFactory(GsonConverterFactory.create())  // Convert API response to objects using Gson
            .build()
    }


    @Provides
    @Singleton // Provides ApiService to interact with the API
    fun provideApiService(retrofit: Retrofit): ApiServiceInterface {
        return retrofit.create(ApiServiceInterface::class.java)
    }

    @Provides
    @Singleton  // Provides NetworkInfo utility to check network status
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkInfo {
        return NetworkInfo(context)
    }

    @Provides
    @Singleton // Provides the EncryptedPrefUtil instance for secure preference management
    fun provideEncryptedPrefUtil(@ApplicationContext context: Context): EncryptedPrefUtil {
        return EncryptedPrefUtil(context)
    }

    @Provides
    @Singleton // Provides PreferencesRepository for abstracting preference access
    fun providePreferencesRepository(encryptedPrefUtil: EncryptedPrefUtil): PreferencesRepository {
        return PreferencesRepository(encryptedPrefUtil)
    }

    @Provides
    @Singleton // Provides CorePrefDataSource for managing user session and preferences
    fun provideCorePrefDataSource(preferencesRepository: PreferencesRepository): CorePrefDataSource {
        return CorePrefDataSource(preferencesRepository)
    }

    @Provides
    @Singleton
    fun provideSnackBarManager(@ApplicationContext context: Context): SnackBarManager {
        return SnackBarManager()
    }


    // Provide TokenInterceptor for adding the Authorization token to API requests
    @Provides
    @Singleton // Singleton ensures one instance of TokenInterceptor
    fun provideTokenInterceptor(corePrefDataSource: CorePrefDataSource): ApiInterceptor {
        return ApiInterceptor(corePrefDataSource)  // TokenInterceptor retrieves the token from CorePrefDataSource and adds it to API requests
    }

    @Provides
    @Singleton
    fun providePokemonListRepository(
        pokemonListRemoteDataSource: PokemonListRemoteDataSource
    ): PokemonListRepository {
        return PokemonListDataRepository(pokemonListRemoteDataSource)
    }

    @Provides
    @Singleton
    fun providePokemonDetailsRepository(
        pokemonListRemoteDataSource: PokemonDetailRemoteDataSource
    ): PokemonDetailRepository {
        return PokemonDetailDataRepository(pokemonListRemoteDataSource)
    }


//    @Provides
//    @Singleton // provide to future repos like this
//    fun provideAuthRepository(
//        authRemoteDataSource: AuthRemoteDataSource,
//        networkInfo: NetworkInfo,
//        corePrefDataSource: CorePrefDataSource
//    ): AuthRepository {
//        return DataAuthRepository(authRemoteDataSource, networkInfo, corePrefDataSource)
//    }
}
