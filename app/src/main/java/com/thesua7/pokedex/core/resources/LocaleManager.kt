package com.thesua7.pokedex.core.resources

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.util.Locale

object LocaleManager {
    private const val TAG = "LocaleHelper"

    fun setLocale(context: Context, languageCode: String): Context {
        Timber.tag(TAG).d("Setting locale to: $languageCode")

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                setLocalePostTiramisu(context, languageCode)
            else -> setLocalePriorToTiramisu(context, languageCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setLocalePostTiramisu(context: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(languageCode)
        val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
        localeManager.applicationLocales = LocaleList(locale)

        Timber.tag(TAG).d("Locale set using LocaleManager: ${locale.displayName}")
        return context
    }

    private fun setLocalePriorToTiramisu(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))

        val updatedContext = context.createConfigurationContext(configuration)

        Timber.tag(TAG).d("Locale set using Configuration: ${locale.displayName}")
        return updatedContext
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
            localeManager.applicationLocales.get(0) ?: Locale.getDefault()
        } else {
            context.resources.configuration.locales.get(0) ?: Locale.getDefault()
        }.also {
            Timber.tag(TAG).d("Current locale: ${it.displayName}")
        }
    }

    fun getSupportedLocales(): List<Locale> = listOf(
        Locale("en"),  // English
        Locale("ja"),  // Japanese
        // Add more supported locales here
    )
}