package com.thesis.arrivo.components.other_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.thesis.arrivo.view_models.AuthViewModel

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    aurhViewModel: AuthViewModel,
    label: String,
    showVisualityToggleIcon: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String
) {
    val trailingIcon = if (showVisualityToggleIcon) aurhViewModel.visibilityIcon else null

    AppTextField(
        modifier = modifier,
        value = value,
        label = label,
        singleLine = true,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next,
        leadingIcon = Icons.Outlined.Lock,
        trailingIcon = trailingIcon,
        visualTransformation = if (showVisualityToggleIcon) aurhViewModel.passwordVisualTransformation else PasswordVisualTransformation(),
        onValueChange = onValueChange,
        isError = isError,
        errorMessage = errorMessage
    )
}