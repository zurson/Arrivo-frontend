package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

fun changeActivity(context: Context, destActivity: KClass<*>, finish: Boolean = false) {
    val intent = Intent(context, destActivity.java)
    context.startActivity(intent)

    if (finish) (context as? Activity)?.finish()
}