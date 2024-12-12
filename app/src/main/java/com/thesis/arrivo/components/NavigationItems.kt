package com.thesis.arrivo.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FormatListNumberedRtl
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.ui.graphics.vector.ImageVector
import com.thesis.arrivo.R

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector,
) {

    data object Tasks : NavigationItem(
        route = "tasks",
        title = R.string.nav_delivery_details_label,
        icon = Icons.Outlined.DirectionsCar
    )

    data object Map : NavigationItem(
        route = "map",
        title = R.string.nav_map_label,
        icon = Icons.Outlined.LocationOn
    )

    data object Accidents : NavigationItem(
        route = "accidents",
        title = R.string.nav_road_accident_label,
        icon = Icons.Outlined.ReportProblem
    )

    data object Reports : NavigationItem(
        route = "reports",
        title = R.string.nav_your_reports_label,
        icon = Icons.Outlined.FormatListNumberedRtl
    )

    data object Account : NavigationItem(
        route = "account",
        title = R.string.nav_help_label,
        icon = Icons.Outlined.AccountCircle
    )
}