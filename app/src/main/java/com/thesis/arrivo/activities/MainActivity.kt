package com.thesis.arrivo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.FirebaseApp
import com.thesis.arrivo.R
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme

class MainActivity : AppCompatActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext

        initializeFirebase()
        val placesClient = initializePlacesClient()

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainView(placesClient = placesClient)
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

}