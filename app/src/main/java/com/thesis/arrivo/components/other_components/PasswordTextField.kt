package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp
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
    OutlinedTextField(
        modifier = modifier,
        value = value,
        singleLine = true,
        textStyle = TextStyle.Default.copy(fontSize = dpToSp(R.dimen.form_text_size)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),

        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,
            errorLeadingIconColor = MaterialTheme.colorScheme.error
        ),

        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "",
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.form_ic_padding))
                    .size(dimensionResource(R.dimen.form_ic_size))
            )
        },

        trailingIcon = {
            if (showVisualityToggleIcon) {
                IconButton(
                    onClick = { aurhViewModel.togglePasswordVisibility() },
                    Modifier.padding(dimensionResource(R.dimen.form_ic_padding))
                ) {
                    Icon(
                        imageVector = aurhViewModel.visibilityIcon,
                        contentDescription = null,
                        Modifier.size(dimensionResource(R.dimen.form_ic_size)),
                    )
                }
            }

        },

        visualTransformation = if (showVisualityToggleIcon) aurhViewModel.passwordVisualTransformation else PasswordVisualTransformation(),

        onValueChange = onValueChange,

        label = { Text(text = label, fontSize = dpToSp(R.dimen.form_label_text_size)) },

        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = dpToSp(R.dimen.form_error_size)
                )
            }
        },
        isError = isError

    )
}