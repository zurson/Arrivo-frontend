package com.thesis.arrivo.view_models

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.ui.authentication.FirebaseAuthManager
import com.thesis.arrivo.utilities.showToast

class AuthViewModel(
    val mainScaffoldViewModel: MainScaffoldViewModel
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var repeatedPassword by mutableStateOf("")

    var passwordVisible by mutableStateOf(false)
        private set

    val passwordVisualTransformation: VisualTransformation
        get() = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val visibilityIcon
        get() = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    var isErrorEmail by mutableStateOf(false)
    var isErrorPassword by mutableStateOf(false)
    var isErrorRepeatPassword by mutableStateOf(false)


    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }


    fun validateEmail(): Boolean {
        isErrorEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return !isErrorEmail
    }


    fun validatePassword(): Boolean {
        isErrorPassword = password.length < 6
        return !isErrorPassword
    }


    fun validateRepeatPassword(): Boolean {
        isErrorRepeatPassword = password != repeatedPassword
        return !isErrorRepeatPassword
    }


//    fun registerViaEmail(context: Context) {
//        FirebaseAuthManager().registerViaEmail(email, password) { authStatus ->
//            if (authStatus.success) {
//                mainScaffoldViewModel.onLoginSuccess()
//            } else {
//
//            }
//        }
//    }


    fun loginViaEmail(context: Context) {
        FirebaseAuthManager().loginViaEmail(email, password) { authStatus ->
            if (authStatus.success) {
                mainScaffoldViewModel.onAuthenticationSuccess()
            } else {
                authStatus.exception?.let {
                    showToast(
                        context,
                        authStatus.exception.message,
                        toastLength = Toast.LENGTH_LONG
                    )
                }

            }
        }
    }
}