package com.arfoxcode.rotationlock.data

/**
 * Represents the current state of the rotation controller.
 */
data class RotationState(
    val activeMode: OrientationMode = OrientationMode.AUTO,
    val hasWriteSettingsPermission: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isLocked: Boolean get() = activeMode.isLocked
}
