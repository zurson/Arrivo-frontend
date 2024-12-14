package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.components.bounceClick
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