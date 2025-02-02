package com.thesis.arrivo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.thesis.arrivo.R
import com.thesis.arrivo.components.permissions.RequiredPermissionsScreen
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.PermissionManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.Settings.Companion.REQUIRED_PERMISSION
import com.thesis.arrivo.utilities.changeActivity
import com.thesis.arrivo.utilities.location.PlacesApiHelper
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager
import com.thesis.arrivo.utilities.notifications.Notifier

class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private var wasRestarted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        initializeApp()

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainContent()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        NavigationApiManager.stopNavigation()
    }


    override fun onResume() {
        super.onResume()

        if (wasRestarted) return

        val granted =
            PermissionManager.isPermissionGranted(REQUIRED_PERMISSION)
        if (granted) return

        restartApp()
    }


    private fun restartApp() {
        changeActivity(
            this,
            MainActivity::class,
            true
        )

        wasRestarted = true
    }


    @Composable
    fun MainContent() {
        val permissionsChecked = remember { mutableStateOf(false) }
        val locationPermissionGranted = remember { mutableStateOf(false) }

        PermissionManager.RequestMultiplePermissions(Settings.getAskDuringStartPermissions()) {
            permissionsChecked.value = true
            locationPermissionGranted.value = it[REQUIRED_PERMISSION] == true
        }

        when {
            !permissionsChecked.value -> {}
            locationPermissionGranted.value -> {
                MainView()
                wasRestarted = false
            }

            else -> RequiredPermissionsScreen(this)
        }
    }


    private fun initializeApp() {
        initializeFirebase()
        initializePlacesClient()
        initializeNavigationApi()
        initializePermissionsManager()
        initializeNotifier()
    }


    private fun initializeNotifier() {
        Notifier.init(this)
    }


    private fun initializePermissionsManager() {
        PermissionManager.initialize(this)
    }


    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
    }


    private fun initializePlacesClient() {
        if (!Places.isInitialized()) {
            Places.initialize(
                this,
                this.getString(R.string.google_maps_api_key)
            )
        }

        PlacesApiHelper.init(Places.createClient(this))
    }


    private fun initializeNavigationApi() {
        NavigationApiManager.initialize(this)
    }

}

