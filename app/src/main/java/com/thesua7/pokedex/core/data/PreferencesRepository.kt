package com.thesua7.pokedex.core.data


import com.thesua7.pokedex.core.data.local.EncryptedPrefUtil
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val encryptedPrefUtil: EncryptedPrefUtil
) {
    fun getString(key: String, defaultValue: String? = null): String? {
        return encryptedPrefUtil.getString(key, defaultValue)
    }

    fun setString(key: String, value: String) {
        encryptedPrefUtil.setString(key, value)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return encryptedPrefUtil.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        encryptedPrefUtil.setBoolean(key, value)
    }

    fun setInt(key: String,value: Int){
        encryptedPrefUtil.setInt(key, value)
    }

    fun getInt(key: String,defaultValue: Int = -1): Int {
        return encryptedPrefUtil.getInt(key, defaultValue)
    }

    fun clearAll() {
        encryptedPrefUtil.clearAll()
    }

    // Add other methods as needed
}
