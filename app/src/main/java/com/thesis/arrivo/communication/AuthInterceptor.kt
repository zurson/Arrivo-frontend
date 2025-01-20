package com.thesis.arrivo.communication

import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.R
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.utilities.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val tokenTask = FirebaseAuth.getInstance().currentUser?.getIdToken(false)
            val token = tokenTask?.result?.token

            val request = chain.request().newBuilder().apply {
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }.build()

            if (!isNetworkAvailable())
                throw IOException(MainActivity.context.getString(R.string.io_error))

            return chain.proceed(request)

        } catch (e: Exception) {
            throw IOException(e)
        }
    }

}