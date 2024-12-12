package com.thesis.arrivo.ui.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    leadingIcon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        singleLine = true,
        textStyle = TextStyle.Default.copy(
            fontSize = dpToSp(R.dimen.auth_form_text_size)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
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
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background,
            errorLeadingIconColor = MaterialTheme.colorScheme.error
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "",
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.auth_form_ic_padding))
                    .size(dimensionResource(R.dimen.auth_form_ic_size))
            )
        },
        onValueChange = onValueChange,
        label = { Text(
            text = label,
            fontSize = dpToSp(R.dimen.auth_form_label_text_size)
        ) },
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = dpToSp(R.dimen.auth_form_error_size)
                )
            }
        },
        isError = isError
    )
}