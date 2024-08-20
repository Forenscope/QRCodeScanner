package com.grimed.forenscope.qrcodescanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestCameraPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    // Remember the permission request launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    // Check and request permission
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted
                onPermissionGranted()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // Request the permission if not granted
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // Handle case where permission cannot be requested
                onPermissionDenied()
            }
        }
    }
}
