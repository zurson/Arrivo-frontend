package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.thesis.arrivo.R
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String?,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    onLeadingIconClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int? = null,
    readOnly: Boolean = false,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next
) {

    OutlinedTextField(
        visualTransformation = visualTransformation,
        modifier = modifier.heightIn(dimensionResource(R.dimen.text_field_height)),
        value = value,
        singleLine = singleLine,
        readOnly = readOnly,
        maxLines = maxLines,
        textStyle = TextStyle.Default.copy(
            fontSize = dpToSp(R.dimen.form_text_size)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
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
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground
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
        label = {
            label?.let {
                Text(
                    text = label,
                    fontSize = dpToSp(R.dimen.form_label_text_size)
                )
            }
        },
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
