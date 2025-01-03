package com.thesis.arrivo.ui.admin.admin_employees

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.AppTextField
import com.thesis.arrivo.components.FormType
import com.thesis.arrivo.components.LoadingScreen
import com.thesis.arrivo.components.PhoneVisualTransformation
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.EmployeeViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun CreateEmployeeView(mainScaffoldViewModel: MainScaffoldViewModel) {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(mainScaffoldViewModel) }
    val employeeViewModel = remember { EmployeeViewModel() }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* FORMS LIST */
        val (formsListRef) = createRefs()
        val formsListTopGuideline = createGuidelineFromTop(0.1f)
        val formsListBottomGuideline = createGuidelineFromTop(0.75f)

        Forms(
            authViewModel = authViewModel,
            modifier = Modifier.constrainAs(formsListRef) {
                top.linkTo(formsListTopGuideline)
                bottom.linkTo(formsListBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /* CREATE BUTTON */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.87f)
        val buttonBottomGuideline = createGuidelineFromTop(0.95f)

        EmployeeCreateButton(
            context = context,
            navController = mainScaffoldViewModel.navController,
            employeeViewModel = employeeViewModel,
            authViewModel = authViewModel,
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(buttonTopGuideline)
                    bottom.linkTo(buttonBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        LoadingScreen(enabled = employeeViewModel.actionInProgress)
    }
}


@Composable
private fun Forms(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        /**
         * First name
         **/
        BasicTextField(
            value = authViewModel.firstName,
            onValueChange = { authViewModel.onFormValueChange(FormType.FIRST_NAME, it) },
            isErrorVar = authViewModel.isErrorFirstName,
            label = stringResource(R.string.create_account_first_name_label),
            errorMessage = authViewModel.firstNameErrorText,
            trailingIcon = Icons.Outlined.Close,
            maxLength = Settings.FIRST_NAME_MAX_LEN,
        )

        /**
         * Last name
         **/
        BasicTextField(
            value = authViewModel.lastName,
            onValueChange = { authViewModel.onFormValueChange(FormType.LAST_NAME, it) },
            isErrorVar = authViewModel.isErrorLastName,
            label = stringResource(R.string.create_account_last_name_label),
            errorMessage = authViewModel.lastNameErrorText,
            trailingIcon = Icons.Outlined.Close,
            maxLength = Settings.LAST_NAME_MAX_LEN,
        )

        /**
         * Phone number
         **/
        BasicTextField(
            value = authViewModel.phoneNumber,
            onValueChange = { authViewModel.onFormValueChange(FormType.PHONE_NUMBER, it) },
            isErrorVar = authViewModel.isErrorPhoneNumber,
            keyboardType = KeyboardType.Phone,
            label = stringResource(R.string.create_account_phone_label),
            errorMessage = authViewModel.phoneNumberErrorText,
            trailingIcon = Icons.Outlined.Close,
            visualTransformation = PhoneVisualTransformation(),
        )

        /**
         * Email
         **/
        BasicTextField(
            value = authViewModel.email,
            onValueChange = { authViewModel.onFormValueChange(FormType.EMAIL, it) },
            isErrorVar = authViewModel.isErrorEmail,
            keyboardType = KeyboardType.Email,
            label = stringResource(R.string.create_account_email_label),
            errorMessage = authViewModel.emailErrorText,
            trailingIcon = Icons.Outlined.Close,
        )
    }
}


@Composable
private fun EmployeeCreateButton(
    context: Context,
    modifier: Modifier = Modifier,
    employeeViewModel: EmployeeViewModel,
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    AppButton(
        onClick = {
            employeeViewModel.createEmployeeAccount(
                context = context,
                createAccountRequest = authViewModel.prepareEmployeeCreateRequest(),
                onSuccess = { employeeViewModel.onAccountCreateSuccess(context, navController) },
                onFailure = { error -> employeeViewModel.onAccountCreateFailure(context, error) },
            )
        },
        modifier = modifier,
        text = stringResource(R.string.create_account_button_text),
        icon = Icons.Filled.Add
    )
}


@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isErrorVar: Boolean,
    label: String,
    errorMessage: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int? = null,
) {
    AppTextField(
        modifier = modifier
            .fillMaxWidth(),
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        value = value,
        onValueChange = onValueChange,
        keyboardType = keyboardType,
        trailingIcon = trailingIcon,
        onTrailingIconClick = { onValueChange("") },
        label = label,
        isError = isErrorVar,
        errorMessage = errorMessage,
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
        CreateEmployeeView(MainScaffoldViewModel(LocalContext.current, true, rememberNavController()))
    }
}