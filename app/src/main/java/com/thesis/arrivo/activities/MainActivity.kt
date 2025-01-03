package com.thesis.arrivo.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainView()
            }
        }


    }
}