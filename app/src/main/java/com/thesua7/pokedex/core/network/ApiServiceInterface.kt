package com.thesua7.pokedex.core.network

import com.thesua7.pokedex.features.pokemonDetail.data.model.dto.PokemonResponse
import com.thesua7.pokedex.features.pokemonList.data.model.dto.PokemonListResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiServiceInterface {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit:Int,
        @Query("offset") offset:Int
    ):Response<PokemonListResponse>

    @GET("pokemon/{path}")
    suspend fun getPokemon(
        @Path("path") name:String
    ):Response<PokemonResponse>

//    @POST("get-otp")
//    suspend fun sendOtp(@Query("phone") phoneNumber: String): Response<SendOtpResponse>
//
//    @POST("login")
//    suspend fun verifyOtp(
//        @Query("otp") otpCode: String, @Query("user_id") user_id: Int
//    ): Response<VerifyOtpResponse>


}
