package com.thesis.arrivo.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppTextField
import com.thesis.arrivo.components.other_components.PasswordTextField
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.AuthViewModelFactory
import com.thesis.arrivo.view_models.factory.MainScaffoldViewModelFactory


@Composable
fun LoginView(authViewModel: AuthViewModel) {
    authViewModel.manageNavbarOnLogin()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* TOP SECTION */
        val (topSectionRef) = createRefs()
        val topSectionTopGuideline = createGuidelineFromTop(0.05f)
        val topSectionBottomGuideline = createGuidelineFromTop(0.20f)

        TopSection(
            modifier = Modifier.constrainAs(topSectionRef) {
                top.linkTo(topSectionTopGuideline)
                bottom.linkTo(topSectionBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /* FORMS SECTION */
        val (formsRef) = createRefs()
        val formsTopGuideLine = createGuidelineFromTop(0.30f)
        val formsBottomGuideLine = createGuidelineFromTop(0.6f)

        FormsSection(
            authViewModel = authViewModel,
            modifier = Modifier.constrainAs(formsRef) {
                top.linkTo(formsTopGuideLine)
                bottom.linkTo(formsBottomGuideLine)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )


        /* LOGIN BUTTON SECTION */
        val (loginButtonRef) = createRefs()
        val buttonsBottomGuideline = createGuidelineFromBottom(0.05f)

        Box(
            modifier = Modifier
                .constrainAs(loginButtonRef) {
                    bottom.linkTo(buttonsBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth()
        ) {
            AppButton(
                onClick = { authViewModel.onLoginButtonClick() },
                text = stringResource(R.string.login_login_button_text),
            )
        }
    }
}


@Composable
private fun TopSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.primary)

    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.login_title),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = dpToSp(R.dimen.auth_title_text_size),
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )

            Text(
                text = stringResource(R.string.login_help_message),
                maxLines = 2,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = dpToSp(R.dimen.auth_description_text_size),
                modifier = modifier
            )
        }
    }
}


@Composable
private fun FormsSection(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier.fillMaxSize()

    ) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.text_filed_padding)),
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Outlined.Email,
            value = authViewModel.email,
            label = stringResource(R.string.login_email_label),
            onValueChange = { authViewModel.email = it },
            isError = authViewModel.isErrorEmail,
            errorMessage = stringResource(R.string.login_email_error)
        )

        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.text_filed_padding)),
            label = stringResource(R.string.login_password_label),
            onValueChange = { authViewModel.password = it },
            value = authViewModel.password,
            aurhViewModel = authViewModel,
            showVisualityToggleIcon = true,
            isError = authViewModel.isErrorPassword,
            errorMessage = stringResource(R.string.login_password_error)
        )

        Text(
            text = stringResource(R.string.password_reset_button_text),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = dpToSp(R.dimen.auth_reset_password_text_size),
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier
                .bounceClick()
                .clickable { authViewModel.onResetPasswordButtonClick() }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm: MainScaffoldViewModel = viewModel(
        factory = MainScaffoldViewModelFactory(NavigationManager(rememberNavController()))
    )

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            mainScaffoldViewModel = mainVm,
            loadingScreenManager = mainVm
        )
    )

    Theme.ArrivoTheme {
        LoginView(viewModel)
    }
}