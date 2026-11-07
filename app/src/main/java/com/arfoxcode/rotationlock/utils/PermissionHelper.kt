package com.arfoxcode.rotationlock.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Handles WRITE_SETTINGS permission checks and navigation.
 *
 * WRITE_SETTINGS is a "special app access" permission — it cannot be requested
 * via ActivityResultContracts.RequestPermission(). The user MUST navigate to
 * Settings > Apps > Special App Access > Modify system settings manually.
 *
 * canWrite() must be checked every time the app resumes, because the user
 * can revoke this permission at any time from system settings.
 */
object PermissionHelper {

    /**
     * Returns true if the app currently has WRITE_SETTINGS permission.
     * Always call this before attempting any Settings.System.putInt() call.
     */
    fun canWriteSettings(context: Context): Boolean {
        return Settings.System.canWrite(context)
    }

    /**
     * Opens the specific "Modify system settings" page for this app.
     * This is the only way to grant WRITE_SETTINGS on Android 6+.
     */
    fun openWriteSettingsPage(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
