package com.thesis.arrivo.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.thesis.arrivo.ui.MainView
import com.thesis.arrivo.ui.theme.Theme

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            Theme.ArrivoTheme {
                MainView()
            }
        }

    }

}