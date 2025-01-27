package com.thesis.arrivo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.thesis.arrivo.components.permissions.LocationPermissionInfoScreen
import com.thesis.arrivo.components.permissions.RequestLocationPermission
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme

class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        initializeFirebase()
        val placesClient = initializePlacesClient()

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainContent(placesClient)
            }
        }
    }

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun initializePlacesClient(): PlacesClient {
        if (!Places.isInitialized()) {
            Places.initialize(
                this,
                this.getString(R.string.google_maps_api_key)
            )
        }
        return Places.createClient(this)
    }

    @Composable
    fun MainContent(placesClient: PlacesClient) {
        val permissionsChecked = remember { mutableStateOf(false) }
        val permissionsGranted = remember { mutableStateOf(false) }

        RequestLocationPermission { isGranted ->
            permissionsChecked.value = true
            permissionsGranted.value = isGranted
        }

        if (!permissionsChecked.value)
            return

        if (permissionsGranted.value)
            MainView(placesClient)
        else
            LocationPermissionScreen(context)
    }

    @Composable
    fun LocationPermissionScreen(context: Context) {
        LocationPermissionInfoScreen(
            onOpenSettingsClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
    }

}

