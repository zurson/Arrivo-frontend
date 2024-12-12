package com.thesis.arrivo.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.thesis.arrivo.utilities.changeActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeActivity(this, AppActivity::class, true)
    }
}