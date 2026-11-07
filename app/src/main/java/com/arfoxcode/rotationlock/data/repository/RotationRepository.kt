package com.arfoxcode.rotationlock.data.repository

import android.content.Context
import com.arfoxcode.rotationlock.data.OrientationMode
import com.arfoxcode.rotationlock.data.preferences.AppPreferences
import com.arfoxcode.rotationlock.utils.PermissionHelper
import com.arfoxcode.rotationlock.utils.RotationController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RotationRepository(
    private val context: Context,
    private val controller: RotationController,
    private val preferences: AppPreferences
) {

    fun hasWriteSettingsPermission(): Boolean =
        PermissionHelper.canWriteSettings(context)

    fun openWriteSettingsPage() =
        PermissionHelper.openWriteSettingsPage(context)

    fun getCurrentMode(): OrientationMode =
        controller.getCurrentMode()

    suspend fun applyOrientation(mode: OrientationMode): Result<Unit> =
        withContext(Dispatchers.IO) {
            val result = controller.applyOrientation(mode)
            if (result.isSuccess) {
                preferences.setLastOrientation(mode.id)
            }
            result
        }
}
