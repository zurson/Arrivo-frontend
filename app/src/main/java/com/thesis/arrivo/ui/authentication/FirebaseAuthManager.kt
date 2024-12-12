package com.thesis.arrivo.ui.authentication

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun loginViaEmail(email: String, password: String, callback: (AuthStatus) -> Unit) {
        scope.launch {
            val result = loginInternal(email, password)
            callback(result)
        }
    }


    fun registerViaEmail(email: String, password: String, callback: (AuthStatus) -> Unit) {
        scope.launch {
            val result = registerInternal(email, password)
            callback(result)
        }
    }


    private suspend fun loginInternal(email: String, password: String): AuthStatus {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            AuthStatus(success = true, exception = null)
        } catch (e: Exception) {
            AuthStatus(success = false, exception = e)
        }
    }


    private suspend fun registerInternal(email: String, password: String): AuthStatus {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()

            AuthStatus(success = true, exception = null)
        } catch (e: Exception) {
            AuthStatus(success = false, exception = e)
        }
    }


    suspend fun getToken(): String =
        auth.currentUser?.getIdToken(false)?.await()?.token ?: ""


    private fun printToken() = scope.launch {
        val token = getToken()
        Log.i("JWT TOKEN", token)
    }


    fun logoutUser(context: Context) {
        auth.signOut()
    }

}