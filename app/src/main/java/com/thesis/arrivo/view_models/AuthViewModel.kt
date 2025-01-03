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
import com.thesis.arrivo.communication.employee.EmployeeCreateAccountRequest
import com.thesis.arrivo.components.FormType
import com.thesis.arrivo.ui.authentication.FirebaseAuthManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.showToast

class AuthViewModel(
    private val mainScaffoldViewModel: MainScaffoldViewModel
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatedPassword by mutableStateOf("")

    var isErrorFirstName by mutableStateOf(false)
    var isErrorLastName by mutableStateOf(false)
    var isErrorPhoneNumber by mutableStateOf(false)
    var isErrorEmail by mutableStateOf(false)
    var isErrorPassword by mutableStateOf(false)
    var isErrorRepeatPassword by mutableStateOf(false)

    var firstNameErrorText by mutableStateOf("")
    var lastNameErrorText by mutableStateOf("")
    var phoneNumberErrorText by mutableStateOf("")
    var emailErrorText by mutableStateOf("")

    private var passwordVisible by mutableStateOf(false)
        private set

    val passwordVisualTransformation: VisualTransformation
        get() = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    val visibilityIcon
        get() = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff


    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }


    private fun validateEmail(): Boolean {
        isErrorEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return !isErrorEmail
    }


    private fun validatePassword(): Boolean {
        isErrorPassword = password.length < 6
        return !isErrorPassword
    }


    private fun validateRepeatPassword(): Boolean {
        isErrorRepeatPassword = password != repeatedPassword
        return !isErrorRepeatPassword
    }


    fun onLoginButtonClick(context: Context) {
        if (validateEmail() && validatePassword())
            loginViaEmail(context)
    }


    fun prepareEmployeeCreateRequest(): EmployeeCreateAccountRequest {
        return EmployeeCreateAccountRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber
        )
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


    private fun loginViaEmail(context: Context) {
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


    fun onFormValueChange(formType: FormType, input: String) {
        when (formType) {
            FormType.FIRST_NAME -> onFirstNameValueChange(input)
            FormType.LAST_NAME -> onLastNameValueChange(input)
            FormType.PHONE_NUMBER -> onPhoneNumberValueChange(input)
            FormType.EMAIL -> onEmailValueChange(input)
            FormType.PASSWORD -> {}
        }
    }


    private fun onFirstNameValueChange(input: String) {
        firstName = filterBasicTextField(input)
        firstName = firstName.take(Settings.FIRST_NAME_MAX_LEN)
        isErrorFirstName = false
    }


    private fun onLastNameValueChange(input: String) {
        lastName = filterBasicTextField(input)
        lastName = lastName.take(Settings.LAST_NAME_MAX_LEN)
        isErrorLastName = false
    }


    private fun onPhoneNumberValueChange(input: String) {
        phoneNumber = filterPhoneNumber(input)
        phoneNumber = phoneNumber.take(Settings.PHONE_NUMBER_MAX_LEN)
        isErrorPhoneNumber = false
    }


    private fun onEmailValueChange(input: String) {
        email = input.take(Settings.EMAIL_MAX_LEN)
        isErrorEmail = false
    }


    private fun filterBasicTextField(input: String): String {
        return input.replace("[^a-zA-Z]".toRegex(), "")
    }


    private fun filterPhoneNumber(input: String): String {
        return input.replace("[^0-9]".toRegex(), "")
    }

}