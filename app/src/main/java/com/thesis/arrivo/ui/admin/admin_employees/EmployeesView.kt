package com.thesis.arrivo.ui.admin.admin_employees

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun EmployeesView(mainScaffoldViewModel: MainScaffoldViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* EMPLOYEES LIST */
        val (headerImageRef) = createRefs()
        val employeesListTopGuideline = createGuidelineFromTop(0.1f)
        val employeesListBottomGuideline = createGuidelineFromTop(0.86f)

        EmployeesList(modifier = Modifier.constrainAs(headerImageRef) {
            top.linkTo(employeesListTopGuideline)
            bottom.linkTo(employeesListBottomGuideline)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })


        /* CREATE EMPLOYEE BUTTON */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.89f)
        val buttonBottomGuideline = createGuidelineFromTop(0.96f)
        val buttonStartGuideline = createGuidelineFromStart(0.3f)

        CreateEmployeeButton(
            mainScaffoldViewModel = mainScaffoldViewModel,
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(buttonTopGuideline)
                    bottom.linkTo(buttonBottomGuideline)
                    start.linkTo(buttonStartGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

    }
}


@Composable
private fun EmployeesList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(50) { index ->
                Text(
                    text = "Element: $index",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun CreateEmployeeButton(
    modifier: Modifier = Modifier,
    mainScaffoldViewModel: MainScaffoldViewModel
) {
    AppButton(
        onClick = { mainScaffoldViewModel.onCreateEmployeeAccountRedirectButtonClick() },
        modifier = modifier,
        text = "Create Account",
        icon = Icons.Filled.Add
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
        EmployeesView(MainScaffoldViewModel(true, rememberNavController()))
    }
}