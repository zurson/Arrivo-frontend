package com.thesis.arrivo.utilities

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

object PermissionManager {

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context
    }


    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    }


    @Composable
    fun RequestPermission(
        permission: String,
        onPermissionResult: (Boolean) -> Unit
    ) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onPermissionResult(isGranted)
        }

        if (!isPermissionGranted(permission)) {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(permission)
            }
        } else {
            onPermissionResult(true)
        }
    }


    @Composable
    fun RequestMultiplePermissions(
        permissions: List<String>,
        onPermissionsResult: (Map<String, Boolean>) -> Unit
    ) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            onPermissionsResult(permissionsMap)
        }

        val shouldRequestPermissions = permissions.any { !isPermissionGranted(it) }

        if (shouldRequestPermissions) {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(permissions.toTypedArray())
            }
        } else {
            onPermissionsResult(permissions.associateWith { true })
        }
    }
}