package com.thesis.arrivo.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.changeActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
//        FirebaseAuth.getInstance().signOut()

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainView()
            }
        }


    }
}