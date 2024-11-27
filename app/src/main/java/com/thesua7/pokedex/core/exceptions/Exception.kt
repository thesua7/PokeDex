package com.thesua7.pokedex.core.exceptions

import retrofit2.Response
import java.io.Serializable






class UiErrorException(uiError: UiError) : Exception(uiError.message)


sealed class UiError(
    val message: String? = null,
    val errorCode: Int? = null,
    private val timestamp: Long = System.currentTimeMillis(),
    var cause: Throwable? = null
) : Serializable {

    data class DatabaseError(
        val dbMessage: String,
        val errorDetails: String? = null
    ) : UiError(message = dbMessage) {
        val errorType = "DatabaseError"
    }

    data class NetworkError(
        val networkMessage: String = "Network error occurred",
        val statusCode: Int? = null
    ) : UiError(message = networkMessage, errorCode = statusCode) {
        val errorType = "NetworkError"
    }

    data class GenericError(
        val errorMessage: String
    ) : UiError(message = errorMessage) {
        val errorType = "GenericError"
    }

    // Logs error details for debugging (replace with your logging solution)
    fun logError() {
        val errorDetails = """
            Error: $message
            Code: $errorCode
            Timestamp: $timestamp
            Cause: ${cause?.javaClass?.simpleName ?: "None"}
        """.trimIndent()
        println(errorDetails) // Replace with Log.d or Timber, e.g., Log.d("UiError", errorDetails)
    }

    companion object {
        /**
         * Factory method to convert a [Throwable] into a [UiError].
         */
        fun fromThrowable(throwable: Throwable): UiError {
            return when (throwable) {
                is java.net.UnknownHostException -> NetworkError(
                    networkMessage = "No internet connection",
                    statusCode = 404
                )
                is java.sql.SQLException -> DatabaseError(
                    dbMessage = throwable.message ?: "Database error",
                    errorDetails = throwable.localizedMessage
                )
                is java.net.SocketTimeoutException -> NetworkError(
                    networkMessage = "Request timed out",
                    statusCode = 408
                )
                is java.io.IOException -> NetworkError(
                    networkMessage = "Network I/O error occurred"
                )
                else -> GenericError(errorMessage = throwable.message ?: "Unknown error")
            }.apply {
                // Attach the cause for debugging
                this.cause = throwable
            }
        }
    }
}


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val error: UiError) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()
}


suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ResultWrapper.Success(body)
            } else {
                ResultWrapper.Error(UiError.GenericError("Empty response body"))
            }
        } else {
            ResultWrapper.Error(UiError.NetworkError("Network error occurred", response.code()))
        }
    } catch (e: Exception) {
        ResultWrapper.Error(UiError.fromThrowable(e))
    }
}

