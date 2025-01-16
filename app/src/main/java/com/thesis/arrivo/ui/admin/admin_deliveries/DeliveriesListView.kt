package com.thesis.arrivo.ui.admin.admin_deliveries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppHorizontalDivider
import com.thesis.arrivo.components.other_components.AppLegendItem
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.view_models.DeliveriesListViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.DeliveriesListViewModelFactory

@Composable
fun DeliveriesListView(deliveriesListViewModel: DeliveriesListViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* MAIN SECTION */
        val (mainSectionRef) = createRefs()
        val mainSectionTopGuideline = createGuidelineFromTop(0.05f)
        val mainSectionBottomGuideline = createGuidelineFromTop(0.80f)

//        MainSection(
//            deliveryCreateViewModel = deliveryCreateViewModel,
//            modifier = Modifier.constrainAs(mainSectionRef) {
//                top.linkTo(mainSectionTopGuideline)
//                bottom.linkTo(mainSectionBottomGuideline)
//                start.linkTo(startGuideline)
//                end.linkTo(endGuideline)
//                width = Dimension.fillToConstraints
//                height = Dimension.fillToConstraints
//            }
//        )

        /* BOTTOM SECTOR */
        val (bottomSectorListRef) = createRefs()
        val bottomSectorTopGuideline = createGuidelineFromTop(0.82f)

        BottomSector(
            deliveriesListViewModel = deliveriesListViewModel,
            modifier = Modifier.constrainAs(bottomSectorListRef) {
                top.linkTo(bottomSectorTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}


@Composable
private fun BottomSector(
    modifier: Modifier = Modifier,
    deliveriesListViewModel: DeliveriesListViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        AppHorizontalDivider(height = dimensionResource(R.dimen.delivery_list_bottom_sector_divider_height))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.delivery_list_bottom_sector_horizontal_space)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Legend()

            AppButton(
                onClick = { deliveriesListViewModel.onCreateDeliveryButtonClick() },
                text = "Create Delivery",
                icon = Icons.Filled.Add,
            )
        }
    }
}


@Composable
private fun Legend() {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        DeliveryStatus.entries.forEach { deliveryStatus ->
            AppLegendItem(
                filterName = DeliveriesListViewModel.getRenamedFilter(deliveryStatus),
                circleColor = DeliveriesListViewModel.getFilterColor(deliveryStatus)
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainScaffoldViewModel(
        navigationManager = NavigationManager(rememberNavController()),
        context = LocalContext.current,
        adminMode = true
    )

    val deliveriesListView: DeliveriesListViewModel = viewModel(
        factory = DeliveriesListViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            navigationManager = NavigationManager(rememberNavController()),
        )
    )

    Theme.ArrivoTheme {
        DeliveriesListView(deliveriesListView)
    }
}