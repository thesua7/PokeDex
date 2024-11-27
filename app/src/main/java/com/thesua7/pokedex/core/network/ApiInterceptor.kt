package com.thesua7.pokedex.core.network


import com.thesua7.pokedex.core.data.local.CorePrefDataSource
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

// ApiInterceptor - Adds the token to every API request
class ApiInterceptor @Inject constructor(
    private val corePrefDataSource: CorePrefDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Retrieve the token from CorePrefDataSource
        val token = corePrefDataSource.getUserToken()

        // Log the token (for debugging purposes, remove in production)
        Timber.tag("ApiInterceptor").d("Token: $token")

        // Add the token to the request if it exists
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")  // Add token header
            } else {
                Timber.tag("ApiInterceptor").w("Token is missing or empty.")
            }
        }.build()

        return chain.proceed(request)
    }
}
