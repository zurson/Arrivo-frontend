package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSpinner(
    items: List<String>,
    label: String,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelection by remember { mutableStateOf(selectedItem) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = currentSelection,
                singleLine = true,
                label = {
                    Text(
                        text = label,
                        fontSize = dpToSp(R.dimen.form_label_text_size)
                    )
                },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = TextStyle.Default.copy(fontSize = dpToSp(R.dimen.form_text_size)),
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

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontSize = dpToSp(R.dimen.app_spinner_item_text_size),
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.app_spinner_item_padding))
                            )
                        },
                        onClick = {
                            currentSelection = item
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
