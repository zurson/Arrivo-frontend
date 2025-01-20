package com.thesis.arrivo.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppTextField
import com.thesis.arrivo.components.other_components.PasswordTextField
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.AuthViewModelFactory


@Composable
fun LoginView(
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    authViewModel.manageNavbarOnLogin()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* INFO SECTION */
        val (infoTitleRef, infoDescRef, infoImageRef) = createRefs()

        val infoTopGuideline = createGuidelineFromTop(0.05f)
        val infoBottomGuideline = createGuidelineFromTop(0.20f)


        LoginImage(modifier = Modifier
            .constrainAs(infoImageRef) {
                top.linkTo(infoTopGuideline)
                bottom.linkTo(infoBottomGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .padding(horizontal = 16.dp))

        LoginTitle(
            text = stringResource(R.string.login_title),
            modifier = Modifier.constrainAs(infoTitleRef) {
                top.linkTo(infoImageRef.top)
                bottom.linkTo(infoDescRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        LoginDescription(modifier = Modifier.constrainAs(infoDescRef) {
            top.linkTo(infoTitleRef.bottom)
            bottom.linkTo(infoBottomGuideline)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })


        /* FORMS SECTION */

        val (formsRef) = createRefs()
        val formsTopGuideLine = createGuidelineFromTop(0.30f)
        val formsBottomGuideLine = createGuidelineFromTop(0.55f)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(formsRef) {
                top.linkTo(formsTopGuideLine)
                bottom.linkTo(formsBottomGuideLine)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }

        ) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.text_filed_padding)
                    ),
                keyboardType = KeyboardType.Email,
                leadingIcon = Icons.Outlined.Email,
                value = authViewModel.email,
                label = stringResource(R.string.login_email_label),
                onValueChange = { authViewModel.email = it },
                isError = authViewModel.isErrorEmail,
                errorMessage = stringResource(R.string.login_email_error)
            )

            Spacer(modifier = Modifier.height(8.dp))

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
        }


        /* BUTTONS SECTION */

        val (loginButtonRef) = createRefs()
        val buttonsTopGuideline = createGuidelineFromTop(0.77f)
        val buttonsBottomGuideline = createGuidelineFromTop(0.85f)
        val buttonsStartGuideline = createGuidelineFromStart(0.05f)
        val buttonsEndGuideline = createGuidelineFromEnd(0.05f)


        /* Login Button */

        CustomOutlinedButton(
            onClick = { authViewModel.onLoginButtonClick(context) },
            modifier = Modifier.constrainAs(loginButtonRef) {
                top.linkTo(buttonsTopGuideline)
                bottom.linkTo(buttonsBottomGuideline)
                start.linkTo(buttonsStartGuideline)
                end.linkTo(buttonsEndGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = stringResource(R.string.login_login_button_text),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = dpToSp(R.dimen.auth_button_text_size)
            )
        }


        /* TERMS SECTION */
        val (termsRef) = createRefs()

        Text(
            text = stringResource(R.string.login_terms),
            fontSize = dpToSp(R.dimen.auth_terms_text_size),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(termsRef) {
                    top.linkTo(buttonsBottomGuideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

    }
}


@Composable
fun LoginImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 50.dp,
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp
                )
            )
            .background(MaterialTheme.colorScheme.primary)

    )
}

@Composable
fun LoginTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        maxLines = 1,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = dpToSp(R.dimen.auth_title_text_size),
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun LoginDescription(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.login_help_message),
        maxLines = 2,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = dpToSp(R.dimen.auth_description_text_size),
        modifier = modifier
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Show() {
    val mainVm = MainScaffoldViewModel(
        NavigationManager(rememberNavController())
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