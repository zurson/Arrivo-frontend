package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.components.NavigationItem
import retrofit2.HttpException
import java.io.IOException
import kotlin.reflect.KClass

fun runOnMainThread(
    action: () -> Unit
) {
    Handler(Looper.getMainLooper()).post {
        action()
    }
}


fun changeActivity(context: Context, destActivity: KClass<*>, finish: Boolean = false) {
    runOnMainThread {
        val intent = Intent(context, destActivity.java)
        context.startActivity(intent)

        if (finish) (context as? Activity)?.finish()
    }
}


@Composable
fun dpToSp(@DimenRes id: Int): TextUnit {
    return with(LocalDensity.current) { dimensionResource(id).toSp() }
}


fun navigateTo(
    navController: NavHostController,
    navigationItem: NavigationItem,
    clearHistory: Boolean = false
) {
    runOnMainThread {
        navController.navigate(navigationItem.route) {
            if (clearHistory) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}


fun showToast(context: Context, text: String?, toastLength: Int = Toast.LENGTH_SHORT) {
    text?.let {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, it, toastLength).show()
        }
    }
}


fun mapError(e: Exception, context: Context): ErrorResponse {
    e.printStackTrace()
    return when (e) {
        is HttpException -> {
            val errorBody = e.response()?.errorBody()?.string()
            val errors = parseErrorResponse(context, errorBody)
            ErrorResponse(e.code(), errors)
        }

        is IOException ->
            ErrorResponse(
                -1,
                listOf(context.getString(R.string.io_error))
            )

        else -> ErrorResponse(-1, listOf(context.getString(R.string.unexpected_error)))
    }

}


fun parseErrorResponse(context: Context, errorBody: String?): List<String> {
    return try {
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse.errors
    } catch (e: Exception) {
        listOf(context.getString(R.string.unexpected_error))
    }
}


fun showErrorDialog(context: Context, title: String, errorResponse: ErrorResponse) {
    val errorMessage = if (errorResponse.errors.isNotEmpty()) {
        errorResponse.errors.joinToString(separator = "\n") { "- $it" }
    } else {
        context.getString(R.string.unexpected_error)
    }

    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage("Code: ${errorResponse.code}\n\n$errorMessage")
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}