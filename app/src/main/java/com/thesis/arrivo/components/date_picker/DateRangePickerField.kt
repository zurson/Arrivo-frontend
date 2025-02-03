package com.thesis.arrivo.components.date_picker

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppTextField
import com.thesis.arrivo.utilities.convertMillisToDate
import com.thesis.arrivo.utilities.getCurrentDateMillis

@Composable
fun DateRangePickerField(
    modifier: Modifier = Modifier,
    selectedDates: Pair<Long, Long>,
    onDateRangeSelected: (Pair<Long, Long>) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var showModal by remember { mutableStateOf(false) }

    val firstDate = convertMillisToDate(selectedDates.first)
    val secondDate = convertMillisToDate(selectedDates.second)

    Box(
        modifier = modifier
    ) {
        AppTextField(
            value = "$firstDate - $secondDate",
            onValueChange = { },
            label = stringResource(R.string.date_picker_label),
            readOnly = true,
            trailingIcon = Icons.Default.DateRange,
            isError = isError,
            errorMessage = errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(selectedDates) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) showModal = true
                    }
                }
        )
    }

    if (showModal) {
        DatePickerModals.Range(
            onDateRangeSelected = {
                val first = it.first ?: getCurrentDateMillis()
                val second = it.second ?: getCurrentDateMillis()
                onDateRangeSelected(Pair(first, second))
            },
            onDismiss = { showModal = false }
        )
    }
}