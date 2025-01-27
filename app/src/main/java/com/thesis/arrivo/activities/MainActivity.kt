package com.thesis.arrivo.activities

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
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.FirebaseApp
import com.thesis.arrivo.R
import com.thesis.arrivo.components.permissions.LocationPermissionScreen
import com.thesis.arrivo.components.permissions.RequestLocationPermission
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.location.PlacesApiHelper

class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        initializeFirebase()
        initializePlacesClient()

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainContent()
            }
        }
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

    @Composable
    fun MainContent() {
        val permissionsChecked = remember { mutableStateOf(false) }
        val permissionsGranted = remember { mutableStateOf(false) }

        RequestLocationPermission { isGranted ->
            permissionsChecked.value = true
            permissionsGranted.value = isGranted
        }

        when {
            !permissionsChecked.value -> {}
            permissionsGranted.value -> MainView()
            else -> LocationPermissionScreen(this)
        }
    }
}

