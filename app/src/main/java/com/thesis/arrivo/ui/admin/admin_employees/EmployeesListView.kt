package com.thesis.arrivo.ui.admin.admin_employees

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.view_models.EmployeeViewModel
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.factory.EmployeeViewModelFactory

@Composable
fun EmployeesListView(
    employeeViewModel: EmployeeViewModel,
    mainViewModel: MainViewModel
) {
    val employees by employeeViewModel.employees.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        ShowEmployeeDetails(employeeViewModel = employeeViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* EMPLOYEES LIST */
        val (headerImageRef) = createRefs()
        val employeesListTopGuideline = createGuidelineFromTop(0.05f)
        val employeesListBottomGuideline = createGuidelineFromTop(0.85f)

        EmployeesList(
            employees = employees,
            employeeViewModel = employeeViewModel,
            loadingScreenStatusChecker = mainViewModel,
            modifier = Modifier.constrainAs(headerImageRef) {
                top.linkTo(employeesListTopGuideline)
                bottom.linkTo(employeesListBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* BUTTONS */
        val (buttonsRef) = createRefs()
        val buttonsTopGuideline = createGuidelineFromTop(0.86f)

        ButtonsSection(
            employeeViewModel = employeeViewModel,
            modifier = Modifier
                .constrainAs(buttonsRef) {
                    top.linkTo(buttonsTopGuideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )
    }
}


@Composable
private fun ShowEmployeeDetails(employeeViewModel: EmployeeViewModel) {
    if (employeeViewModel.showEmployeeDetails)
        EmployeesDetailsAlertDialog(
            emp = employeeViewModel.clickedEmployee,
            onDismiss = { employeeViewModel.toggleShowEmployeeDetails() },
            onEditButtonClick = { employeeViewModel.onEmployeeEditButtonClick() }
        )
}


@Composable
private fun EmployeesList(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel,
    employees: List<Employee>,
    loadingScreenStatusChecker: LoadingScreenStatusChecker
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        if (employees.isEmpty()) {
            EmptyList(loadingScreenStatusChecker)
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(employees) { emp ->
                EmployeeContainer(
                    employeeViewModel = employeeViewModel,
                    employee = emp
                )
            }
        }
    }
}


@Composable
private fun EmployeeContainer(
    employeeViewModel: EmployeeViewModel,
    employee: Employee
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick()
                .clickable {
                    employeeViewModel.clickedEmployee = employee
                    employeeViewModel.toggleShowEmployeeDetails()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.requiredSize(dimensionResource(R.dimen.employees_icon_size))
                )

                Text(
                    text = "${employee.firstName} ${employee.lastName}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = dpToSp(R.dimen.employees_data_text_size),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.requiredSize(dimensionResource(R.dimen.employees_icon_size))
                )
            }

        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
    }
}


@Composable
private fun ButtonsSection(
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = modifier
            .fillMaxSize()
    ) {
        AppButton(
            onClick = { employeeViewModel.onWorkTimeAnalysisButtonClick() },
            text = stringResource(R.string.work_time_employees_list_button_text),
            icon = Icons.Outlined.Analytics,
            modifier = Modifier.weight(1f)
        )

        AppButton(
            onClick = { employeeViewModel.onCreateEmployeeAccountButtonClick() },
            text = stringResource(R.string.create_account_redirect_button_text),
            icon = Icons.Filled.Add,
            modifier = Modifier.weight(1f),
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm = MainViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val viewModel: EmployeeViewModel = viewModel(
        factory = EmployeeViewModelFactory(
            navigationManager = NavigationManager(rememberNavController()),
            loadingScreenManager = mainVm,
            mainViewModel = mainVm,
            context = LocalContext.current
        )
    )

    Theme.ArrivoTheme {
        EmployeesListView(
            mainViewModel = mainVm,
            employeeViewModel = viewModel
        )
    }
}