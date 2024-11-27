package com.thesua7.pokedex.core.base.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesua7.pokedex.core.base.baseState.BaseUiState
import com.thesua7.pokedex.core.exceptions.UiError
import com.thesua7.pokedex.core.exceptions.UiErrorException
import com.thesua7.pokedex.core.extensions.ThreadInfoLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseViewModel<UiState : BaseUiState<*>, UiEvent : Any>(
    private val initialState: UiState,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    // State management
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Event management for one-time events
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    init {
        ThreadInfoLogger.logThreadInfo("BaseViewModel Initialized")
    }

    /**
     * Updates the UI state safely using a transformation.
     */
    fun updateState(transform: UiState.() -> UiState) {
        ThreadInfoLogger.logThreadInfo("Updating State")
        _uiState.update { it.transform() }
    }

    /**
     * Sends a one-time UI event.
     */
    protected fun sendEvent(event: UiEvent) {
        ThreadInfoLogger.logThreadInfo("Sending Event")
        viewModelScope.launch { _uiEvent.emit(event) }
    }

    /**
     * Executes a suspendable task with error handling.
     */
    fun executeWithHandling(
        task: suspend () -> Unit,
        onError: (UiError) -> Unit = { handleGlobalError(it) }
    ) {
        ThreadInfoLogger.logThreadInfo("Executing Task with Handling")
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) { task() }
            } catch (e: Exception) {
                val uiError = UiError.fromThrowable(e)
                Timber.e(e, "Error occurred during task execution")  // Log the error with Timber
                uiError.logError()
                onError(uiError)
            }
        }
    }

    /**
     * Retries a suspendable task with exponential backoff.
     */
    protected fun <T> retryWithBackoff(
        maxRetries: Int = 3,
        initialDelay: Long = 3000L,
        factor: Double = 2.0,
        task: suspend () -> T
    ): Flow<T> = flow {
        var currentDelay = initialDelay
        repeat(maxRetries) { attempt ->
            try {
                ThreadInfoLogger.logThreadInfo("Retrying Task - Attempt $attempt")
                emit(task())  // Emit the result if the task succeeds
                return@flow
            } catch (e: Exception) {
                // On the last retry attempt, convert the caught exception to the appropriate UiError
                if (attempt == maxRetries - 1) {
                    val uiError = UiError.fromThrowable(e)  // Map the exception to UiError
                    Timber.e(e, "Final retry failed after $attempt attempts") // Log the final error
                    uiError.logError()  // Log the error
                    throw UiErrorException(uiError)  // Rethrow the error after the final attempt
                }
                // If it's not the last attempt, delay and retry
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong()  // Increase the delay with the backoff factor
            }
        }
    }.flowOn(ioDispatcher)


    fun resetState() {
        ThreadInfoLogger.logThreadInfo("Resetting UI State")
        _uiState.value = initialState
    }
    /**
     * Executes a database query and processes the result.
     */
    protected fun <T> executeDatabaseQuery(
        query: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (UiError) -> Unit = { handleGlobalError(it) }
    ) {
        ThreadInfoLogger.logThreadInfo("Executing Database Query")
        executeWithHandling(
            task = {
                val result = query()
                onSuccess(result)
            },
            onError = onError
        )
    }

    /**
     * Observes a Flow and processes emitted values or errors.
     */
    protected fun <T> observeDatabaseFlow(
        flow: Flow<T>,
        onEach: (T) -> Unit,
        onError: (UiError) -> Unit = { handleGlobalError(it) }
    ) {
        ThreadInfoLogger.logThreadInfo("Observing Database Flow")
        viewModelScope.launch {
            flow.catch { e ->
                val uiError = UiError.fromThrowable(e)
                Timber.e(e, "Database flow error") // Log the error
                uiError.logError()
                onError(uiError)
            }.collect { result ->
                onEach(result)
            }
        }
    }

    /**
     * Handles global errors by updating the UI state and sending events.
     */
    protected open fun handleGlobalError(error: UiError) {
        updateState {
            copyWith(error = error) as UiState
        }

        globalErrorToEvent(error)?.let { sendEvent(it) }
    }

    /**
     * Converts a global error into a UI event (optional override).
     */
    protected open fun globalErrorToEvent(error: UiError): UiEvent? = null
}
