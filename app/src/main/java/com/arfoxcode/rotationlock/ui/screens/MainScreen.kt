package com.arfoxcode.rotationlock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arfoxcode.rotationlock.R
import com.arfoxcode.rotationlock.data.OrientationMode
import com.arfoxcode.rotationlock.ui.components.OrientationCard
import com.arfoxcode.rotationlock.ui.components.OrientationIconMapper
import com.arfoxcode.rotationlock.ui.components.PermissionBanner
import com.arfoxcode.rotationlock.ui.components.StatusIndicator
import com.arfoxcode.rotationlock.utils.HapticHelper
import com.arfoxcode.rotationlock.viewmodel.RotationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: RotationViewModel,
    onNavigateToSettings: () -> Unit
) {
    val rotationState by viewModel.rotationState.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Show error messages via snackbar
    LaunchedEffect(rotationState.errorMessage) {
        rotationState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_title)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Permission Banner — only shown when permission is missing
            if (!rotationState.hasWriteSettingsPermission) {
                PermissionBanner(
                    onGrantClick = { viewModel.requestWriteSettingsPermission() },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Status Indicator
            StatusIndicator(
                isLocked = rotationState.isLocked,
                activeLabel = stringResource(rotationState.activeMode.labelRes),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Section header
            Text(
                text = "Orientation",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Orientation Grid — 2 columns
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                // Grid height is determined by content; outer scroll handles everything
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        // 3 rows × card height (≈120dp) + spacing
                        ((120 * 3) + (12 * 2)).dp
                    )
            ) {
                items(OrientationMode.entries) { mode ->
                    OrientationCard(
                        mode = mode,
                        icon = OrientationIconMapper.getIcon(mode),
                        isActive = rotationState.activeMode == mode,
                        onClick = {
                            if (hapticEnabled) HapticHelper.performClick(context)
                            viewModel.selectOrientation(mode)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
