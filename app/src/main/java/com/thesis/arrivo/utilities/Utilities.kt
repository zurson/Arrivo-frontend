package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.exceptions.DataCorruptedException
import com.thesis.arrivo.utilities.exceptions.OptimizationFailedException
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        // Optionally finish the current activity
        if (finish) {
            (context as? Activity)?.finish()
        }
    }
}


@Composable
fun dpToSp(@DimenRes id: Int): TextUnit {
    return with(LocalDensity.current) { dimensionResource(id).toSp() }
}


fun showToast(text: String?, toastLength: Int = Toast.LENGTH_SHORT) {
    text?.let {
        runOnMainThread {
            Toast.makeText(MainActivity.context, it, toastLength).show()
        }
    }
}


fun mapError(e: Exception, context: Context): ErrorResponse? {
    e.printStackTrace()

    when (e) {
        is HttpException -> {
            val code = e.code()
            if (code == 401 || code == 500 || code == 403) {
                MainScaffoldViewModel.reset()
                return null
            }

            val errorBody = e.response()?.errorBody()?.string()
            val errors = parseErrorResponse(context, errorBody)

            return ErrorResponse(e.code(), errors)
        }

        is OptimizationFailedException -> {
            return ErrorResponse(-1, listOf(e.message!!))
        }

        is DataCorruptedException -> {
            return ErrorResponse(-1, listOf(e.message!!))
        }

        is IOException ->
            return ErrorResponse(
                code = -1,
                errors = listOf(context.getString(R.string.io_error))
            )

        else -> {
            return ErrorResponse(-1, listOf(context.getString(R.string.unexpected_error)))
        }
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


fun showErrorDialog(context: Context, title: String, errorResponse: ErrorResponse?) {
    if (errorResponse == null)
        return

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


fun localDateToMillis(localDate: LocalDate): Long {
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}


fun preparePhoneCall(context: Context, phoneNumber: String) {
    if (phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } else {
        showToast(
            context.getString(R.string.call_incorrect_phone_number),
            Toast.LENGTH_SHORT
        )
    }
}


fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        MainActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
