package com.thesis.arrivo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.formatPhoneNumber
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.MainScaffoldViewModelFactory


@Composable
fun AccountView(loggedInUserAccessor: LoggedInUserAccessor) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* ACCOUNT IMAGE */
        val (accountImageRef) = createRefs()
        val accountImageTopGuideline = createGuidelineFromTop(0.05f)
        val accountImageBottomGuideline = createGuidelineFromTop(0.3f)

        AccountImageAndName(
            loggedInUserAccessor = loggedInUserAccessor,
            modifier = Modifier.constrainAs(accountImageRef) {
                top.linkTo(accountImageTopGuideline)
                bottom.linkTo(accountImageBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* USER DETAILS SECTOR */
        val (userDetailsRef) = createRefs()
        val userDetailsTopGuideline = createGuidelineFromTop(0.35f)
        val userDetailsBottomGuideline = createGuidelineFromTop(0.8f)

        DetailsSection(
            loggedInUserAccessor = loggedInUserAccessor,
            modifier = Modifier.constrainAs(userDetailsRef) {
                top.linkTo(userDetailsTopGuideline)
                bottom.linkTo(userDetailsBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* BOTTOM SECTOR */
        val (logoutButtonListRef) = createRefs()
        val logoutButtonTopGuideline = createGuidelineFromTop(0.85f)
        val logoutButtonBottomGuideline = createGuidelineFromBottom(0.05f)

        LogoutButton(
            modifier = Modifier.constrainAs(logoutButtonListRef) {
                top.linkTo(logoutButtonTopGuideline)
                bottom.linkTo(logoutButtonBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}


@Composable
private fun AccountImageAndName(
    modifier: Modifier = Modifier,
    loggedInUserAccessor: LoggedInUserAccessor
) {
    val icon = when (loggedInUserAccessor.isAdmin()) {
        true -> Icons.Outlined.AdminPanelSettings
        false -> Icons.Outlined.AccountCircle
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        UserName(loggedInUserAccessor = loggedInUserAccessor)
    }
}


@Composable
private fun UserName(modifier: Modifier = Modifier, loggedInUserAccessor: LoggedInUserAccessor) {
    val user = loggedInUserAccessor.getLoggedInUserDetails()

    val name = when (loggedInUserAccessor.isAdmin()) {
        true -> stringResource(R.string.account_admin_account_name)
        false -> "${user.firstName} ${user.lastName}"
    }

    Text(
        text = name,
        fontSize = dpToSp(R.dimen.account_user_name_text_size),
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
    )
}


@Composable
private fun DetailsSection(
    modifier: Modifier = Modifier,
    loggedInUserAccessor: LoggedInUserAccessor
) {
    val user = loggedInUserAccessor.getLoggedInUserDetails()
    val isAdmin = loggedInUserAccessor.isAdmin()

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
    ) {
        UserDataContainer(
            icon = Icons.Outlined.Email,
            value = user.email
        )

        if (!isAdmin) {
            UserDataContainer(
                icon = Icons.Outlined.Phone,
                value = formatPhoneNumber(user.phoneNumber)
            )

            UserDataContainer(
                icon = Icons.Outlined.StarOutline,
                value = user.status
            )
        }

        UserDataContainer(
            icon = Icons.Outlined.PersonOutline,
            value = user.role
        )
    }
}


@Composable
private fun UserDataContainer(
    icon: ImageVector,
    value: Any
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.requiredSize(dimensionResource(R.dimen.account_details_icon_size))
        )

        Text(
            text = "$value",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.account_details_text_size),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun LogoutButton(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        AppButton(
            onClick = { MainScaffoldViewModel.reset() },
            text = stringResource(R.string.account_logout_button_text)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm: MainScaffoldViewModel = viewModel(
        factory = MainScaffoldViewModelFactory(
            context = LocalContext.current,
            navigationManager = NavigationManager(rememberNavController())
        )
    )

    Theme.ArrivoTheme {
        AccountView(mainVm)
    }
}