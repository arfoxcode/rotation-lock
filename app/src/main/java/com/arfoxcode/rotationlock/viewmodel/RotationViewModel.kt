package com.arfoxcode.rotationlock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arfoxcode.rotationlock.data.OrientationMode
import com.arfoxcode.rotationlock.data.RotationState
import com.arfoxcode.rotationlock.data.preferences.AppPreferences
import com.arfoxcode.rotationlock.data.preferences.ThemeMode
import com.arfoxcode.rotationlock.data.repository.RotationRepository
import com.arfoxcode.rotationlock.utils.RotationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RotationViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = AppPreferences(application)
    private val controller = RotationController(application)
    private val repository = RotationRepository(application, controller, preferences)

    private val _rotationState = MutableStateFlow(RotationState())
    val rotationState: StateFlow<RotationState> = _rotationState.asStateFlow()

    val themeMode: StateFlow<ThemeMode> = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    val hapticEnabled: StateFlow<Boolean> = preferences.hapticFeedback
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        refreshState()
    }

    /**
     * Called from onResume to pick up permission changes made in system settings.
     */
    fun refreshState() {
        val hasPermission = repository.hasWriteSettingsPermission()
        val currentMode = if (hasPermission) repository.getCurrentMode() else OrientationMode.AUTO
        _rotationState.update {
            it.copy(
                hasWriteSettingsPermission = hasPermission,
                activeMode = currentMode,
                errorMessage = null
            )
        }
    }

    fun selectOrientation(mode: OrientationMode) {
        if (!_rotationState.value.hasWriteSettingsPermission) {
            _rotationState.update {
                it.copy(errorMessage = "Permission required. Please grant 'Modify system settings'.")
            }
            return
        }

        viewModelScope.launch {
            _rotationState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.applyOrientation(mode)

            result.fold(
                onSuccess = {
                    _rotationState.update {
                        it.copy(
                            activeMode = mode,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _rotationState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to change rotation."
                        )
                    }
                }
            )
        }
    }

    fun requestWriteSettingsPermission() {
        repository.openWriteSettingsPage()
    }

    fun clearError() {
        _rotationState.update { it.copy(errorMessage = null) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferences.setThemeMode(mode)
        }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setHapticFeedback(enabled)
        }
    }
}
