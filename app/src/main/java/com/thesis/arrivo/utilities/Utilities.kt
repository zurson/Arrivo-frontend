package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
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