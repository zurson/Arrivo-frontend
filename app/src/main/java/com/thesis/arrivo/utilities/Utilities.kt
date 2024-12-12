package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavHostController
import com.thesis.arrivo.components.NavigationItem
import kotlin.reflect.KClass

fun changeActivity(context: Context, destActivity: KClass<*>, finish: Boolean = false) {
    val intent = Intent(context, destActivity.java)
    context.startActivity(intent)

    if (finish) (context as? Activity)?.finish()
}

@Composable
fun dpToSp(@DimenRes id: Int): TextUnit {
    return with(LocalDensity.current) { dimensionResource(id).toSp() }
}


fun navigateTo(navController: NavHostController, navigationItem: NavigationItem) {
    navController.navigate(navigationItem.route)
}


fun showToast(context: Context, text: String?, toastLength: Int = Toast.LENGTH_SHORT) {
    text?.let {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, it, toastLength).show()
        }
    }
}