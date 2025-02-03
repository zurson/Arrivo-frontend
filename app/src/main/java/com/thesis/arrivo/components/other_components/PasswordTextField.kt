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
    authViewModel: AuthViewModel,
    label: String,
    showVisibilityToggleIcon: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String
) {
    val trailingIcon = if (showVisibilityToggleIcon) authViewModel.visibilityIcon else null

    AppTextField(
        modifier = modifier,
        value = value,
        label = label,
        singleLine = true,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next,
        leadingIcon = Icons.Outlined.Lock,
        trailingIcon = trailingIcon,
        onTrailingIconClick = { authViewModel.togglePasswordVisibility() },
        visualTransformation = if (showVisibilityToggleIcon) authViewModel.passwordVisualTransformation else PasswordVisualTransformation(),
        onValueChange = onValueChange,
        isError = isError,
        errorMessage = errorMessage
    )
}