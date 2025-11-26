package io.github.lazulikao.remotemate.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LanguageManager {
    const val KEY_LANGUAGE = "app_language"
    const val LANGUAGE_FOLLOW_SYSTEM = "follow_system"
    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_CHINESE = "zh"
    const val LANGUAGE_ARABIC = "ar"
    const val LANGUAGE_PERSIAN = "fa"

    private const val PREFS_NAME = "language_settings"

    fun setLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_FOLLOW_SYSTEM) ?: LANGUAGE_FOLLOW_SYSTEM
    }

    fun applyLanguage(context: Context): Context {
        val language = getLanguage(context)
        if (language == LANGUAGE_FOLLOW_SYSTEM) {
            return context
        }

        val locale = when (language) {
            LANGUAGE_ENGLISH -> Locale.ENGLISH
            LANGUAGE_CHINESE -> Locale.SIMPLIFIED_CHINESE
            LANGUAGE_ARABIC -> Locale("ar")
            LANGUAGE_PERSIAN -> Locale("fa")
            else -> return context
        }

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getLanguageName(context: Context, language: String): String {
        return when (language) {
            LANGUAGE_FOLLOW_SYSTEM -> context.getString(io.github.lazulikao.remotemate.R.string.language_follow_system)
            LANGUAGE_ENGLISH -> context.getString(io.github.lazulikao.remotemate.R.string.language_english)
            LANGUAGE_CHINESE -> context.getString(io.github.lazulikao.remotemate.R.string.language_chinese)
            LANGUAGE_ARABIC -> context.getString(io.github.lazulikao.remotemate.R.string.language_arabic)
            LANGUAGE_PERSIAN -> context.getString(io.github.lazulikao.remotemate.R.string.language_persian)
            else -> language
        }
    }

    fun getAllLanguages(): List<Pair<String, String>> {
        return listOf(
            LANGUAGE_FOLLOW_SYSTEM to "language_follow_system",
            LANGUAGE_ENGLISH to "language_english",
            LANGUAGE_CHINESE to "language_chinese",
            LANGUAGE_ARABIC to "language_arabic",
            LANGUAGE_PERSIAN to "language_persian"
        )
    }
}
