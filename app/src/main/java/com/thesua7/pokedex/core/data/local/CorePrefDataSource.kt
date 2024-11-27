package com.thesua7.pokedex.core.data.local

import com.thesua7.pokedex.core.data.PreferencesRepository
import javax.inject.Inject

class CorePrefDataSource @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    companion object {
        const val KEY_USER_TOKEN = "USER_TOKEN"
        const val KEY_USER_ID = "USER_ID"
        const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
        const val KEY_CURRENT_APP_LANGUAGE = "CURRENT_LANGUAGE"
    }

    fun saveUserToken(token: String) {
        preferencesRepository.setString(KEY_USER_TOKEN, token)
    }

    fun saveUserId(userId: Int) {
        preferencesRepository.setInt(KEY_USER_ID, userId)
    }

    fun getUserId(): Int {
        return preferencesRepository.getInt(KEY_USER_ID)
    }

    fun getUserToken(): String? {
        return preferencesRepository.getString(KEY_USER_TOKEN)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        preferencesRepository.setBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
    }

    fun isLoggedIn(): Boolean {
        return preferencesRepository.getBoolean(KEY_IS_LOGGED_IN)
    }

    fun saveLanguagePreference(language: String) {
        preferencesRepository.setString(KEY_CURRENT_APP_LANGUAGE, language)

    }

    fun getLanguagePreference(): String {
        return preferencesRepository.getString(KEY_CURRENT_APP_LANGUAGE) ?: "en"
    }


    fun clearAll() {
        preferencesRepository.clearAll()
    }
}
