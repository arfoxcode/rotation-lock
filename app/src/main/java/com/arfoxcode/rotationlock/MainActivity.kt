package com.arfoxcode.rotationlock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arfoxcode.rotationlock.ui.AppNavigation
import com.arfoxcode.rotationlock.ui.theme.RotationLockTheme
import com.arfoxcode.rotationlock.viewmodel.RotationViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RotationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            RotationLockTheme(themeMode = themeMode) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-check permission state whenever the user returns from system settings
        viewModel.refreshState()
    }
}
