package com.thesis.arrivo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    onLeadingIconClick: () -> Unit = {},
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int? = null
) {

    OutlinedTextField(
        visualTransformation = visualTransformation,
        modifier = modifier,
        value = value,
        singleLine = true,
        textStyle = TextStyle.Default.copy(
            fontSize = dpToSp(R.dimen.form_text_size)
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
            leadingIcon?.let {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .bounceClick()
                        .padding(dimensionResource(R.dimen.form_ic_padding))
                        .size(dimensionResource(R.dimen.form_ic_size))
                        .clickable(
                            enabled = leadingIcon != null,
                            onClick = onLeadingIconClick
                        )
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .bounceClick()
                        .padding(dimensionResource(R.dimen.form_ic_padding))
                        .size(dimensionResource(R.dimen.form_ic_size))
                        .clickable(
                            enabled = trailingIcon != null,
                            onClick = onTrailingIconClick
                        )
                )
            }
        },
        onValueChange = onValueChange,
        label = { Text(
            text = label,
            fontSize = dpToSp(R.dimen.form_label_text_size)
        ) },
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = dpToSp(R.dimen.form_error_size)
                )
            } else if (maxLength != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${value.length} / $maxLength",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = dpToSp(R.dimen.form_error_size)
                )
            }
        },
        isError = isError
    )
}
