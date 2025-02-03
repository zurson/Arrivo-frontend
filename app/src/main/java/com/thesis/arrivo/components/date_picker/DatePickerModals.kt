package com.thesis.arrivo.components.date_picker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

object DatePickerModals {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Default(
        onDateSelected: (Long?) -> Unit,
        onDismiss: () -> Unit,
    ) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }) {
                    AcceptButtonText()
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    CancelButtonText()
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Range(
        onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
        onDismiss: () -> Unit
    ) {
        val dateRangePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateRangeSelected(
                            Pair(
                                dateRangePickerState.selectedStartDateMillis,
                                dateRangePickerState.selectedEndDateMillis
                            )
                        )
                        onDismiss()
                    }
                ) {
                    AcceptButtonText()
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    CancelButtonText()
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            )
        }
    }


    @Composable
    private fun CancelButtonText() {
        Text(
            text = stringResource(R.string.date_pick_cancel_button_text),
            fontSize = dpToSp(R.dimen.date_picker_text_size),
            modifier = Modifier.padding(end = 8.dp)
        )
    }


    @Composable
    private fun AcceptButtonText() {
        Text(
            text = stringResource(R.string.date_pick_accept_button_text),
            fontSize = dpToSp(R.dimen.date_picker_text_size),
            modifier = Modifier.padding(end = 8.dp)
        )
    }

}