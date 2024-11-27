package com.thesua7.pokedex.core.base.useCase

import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.core.exceptions.UiErrorException
import com.thesua7.pokedex.core.extensions.ThreadInfoLogger
import com.thesua7.pokedex.core.resources.NetworkInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


abstract class BaseUseCase<in Params, out Result>(
    private val isInternetRequired: Boolean = false
) {
    @Inject
    lateinit var networkInfo: NetworkInfo
    /**
     * Executes the use case with the given parameters.
     * Allows specifying a custom dispatcher, defaulting to Dispatchers.IO.
     */
    suspend operator fun invoke(
        params: Params, dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Result {
        ThreadInfoLogger.logThreadInfo("Executing UseCase: ${this::class.simpleName}")

        if (isInternetRequired && !networkInfo.isInternetAvailable()){
            val networkError = UiError.NetworkError(
                networkMessage = "No Internet Connection",
                statusCode = 404
            )
            throw UiErrorException(networkError)
        }
        networkInfo.observeInternetConnectivity().filter { isConnected -> isConnected }.first()


        return try {
            withContext(dispatcher) { execute(params) }
        } catch (e: Exception) {
            // Log or handle the exception appropriately
            Timber.e(e, "Error occurred during use case execution")
            throw e
        }
    }

    /**
     * Override this method in subclasses to define specific use case logic.
     */
    protected abstract suspend fun execute(params: Params): Result
}
