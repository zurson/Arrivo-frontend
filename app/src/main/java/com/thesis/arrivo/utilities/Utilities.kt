package com.thesis.arrivo.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.components.NavigationItem
import retrofit2.HttpException
import java.io.IOException
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


fun mapError(e: Exception): ErrorResponse {
    e.printStackTrace()
    return when (e) {
        is HttpException -> {
            val errorBody = e.response()?.errorBody()?.string()
            val errors = parseErrorResponse(errorBody)
            ErrorResponse(e.code(), errors)
        }

        is IOException ->
            ErrorResponse(
                -1,
                listOf("Network error. Please check your connection")
        )

        else -> ErrorResponse(-1, listOf("An unexpected error occurred"))
    }

}


fun parseErrorResponse(errorBody: String?): List<String> {
    return try {
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse.errors
    } catch (e: Exception) {
        listOf("Unable to parse error response")
    }
}