package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import com.google.gson.Gson
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.Locale
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


fun showDefaultErrorDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}


fun capitalize(value: String): String {
    return value.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


fun getCurrentDateMillis() = Instant.now().toEpochMilli()


fun convertLongToLocalDate(longVal: Long): LocalDate =
    LocalDate.ofEpochDay(longVal / 86400000L)


fun preparePhoneCall(context: Context, phoneNumber: String) {
    if (phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } else {
        showToast(
            context,
            context.getString(R.string.call_incorrect_phone_number),
            Toast.LENGTH_SHORT
        )
    }
}