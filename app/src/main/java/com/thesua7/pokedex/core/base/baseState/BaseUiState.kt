package com.thesua7.pokedex.core.base.baseState

import com.thesua7.pokedex.core.exceptions.UiError

abstract class BaseUiState<T>(
    open val data: T? = null, open val error: UiError? = null, open val isLoading: Boolean = false
) {

    abstract fun copyWith(
        data: T? = this.data, error: UiError? = this.error, isLoading: Boolean = this.isLoading
    ): BaseUiState<T>
}
