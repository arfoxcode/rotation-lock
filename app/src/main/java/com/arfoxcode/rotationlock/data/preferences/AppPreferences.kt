package com.arfoxcode.rotationlock.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "rotation_lock_prefs"
)

class AppPreferences(private val context: Context) {

    companion object {
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        private val KEY_HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        private val KEY_LAST_ORIENTATION = stringPreferencesKey("last_orientation")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val value = prefs[KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    val hapticFeedback: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HAPTIC_FEEDBACK] ?: true
    }

    val lastOrientation: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_LAST_ORIENTATION] ?: "auto"
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode.name
        }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAPTIC_FEEDBACK] = enabled
        }
    }

    suspend fun setLastOrientation(orientationId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_ORIENTATION] = orientationId
        }
    }
}
