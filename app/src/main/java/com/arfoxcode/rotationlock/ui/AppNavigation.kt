package com.arfoxcode.rotationlock.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arfoxcode.rotationlock.ui.screens.MainScreen
import com.arfoxcode.rotationlock.ui.screens.SettingsScreen
import com.arfoxcode.rotationlock.viewmodel.RotationViewModel

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation(
    viewModel: RotationViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        composable(Routes.MAIN) {
            MainScreen(
                viewModel = viewModel,
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
