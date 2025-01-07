package com.thesis.arrivo.ui.admin.admin_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.libraries.places.api.net.PlacesClient
import com.thesis.arrivo.R
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.AppTextField
import com.thesis.arrivo.components.bounceClick
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.NewTaskViewModel


@Composable
fun TasksView(
    placesClient: PlacesClient
) {
    val newTaskViewModel = remember { NewTaskViewModel(placesClient) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        ShowProductAddAlertDialog(newTaskViewModel = newTaskViewModel)
        ShowLocationSearchDialog(newTaskViewModel = newTaskViewModel)
        ShowProductDeleteConfirmationDialog(newTaskViewModel = newTaskViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* FORMS */
        val (formsRef) = createRefs()
        val formsTopGuideline = createGuidelineFromTop(0.1f)
        val formsBottomGuideline = createGuidelineFromTop(0.87f)

        Forms(
            newTaskViewModel = newTaskViewModel,
            modifier = Modifier.constrainAs(formsRef) {
                top.linkTo(formsTopGuideline)
                bottom.linkTo(formsBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* CREATE TASK BUTTON */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.89f)
        val buttonBottomGuideline = createGuidelineFromTop(0.96f)

        TaskCreateButton(newTaskViewModel = newTaskViewModel,
            modifier = Modifier.constrainAs(buttonRef) {
                top.linkTo(buttonTopGuideline)
                bottom.linkTo(buttonBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

    }
}


@Composable
private fun Forms(
    newTaskViewModel: NewTaskViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.alert_dialog_content_vertical_space)),
        modifier = modifier
            .fillMaxSize()
    ) {

        /**
         * Title
         **/
        AppTextField(
            value = newTaskViewModel.taskTitle,
            onValueChange = { newTaskViewModel.onTaskTitleChange(it) },
            label = stringResource(R.string.new_task_title_label),
            modifier = Modifier.fillMaxWidth(),
            isError = newTaskViewModel.taskTitleError,
            errorMessage = stringResource(R.string.new_task_title_error_message)
        )

        /**
         * Location search
         **/
        AppTextField(
            value = newTaskViewModel.finalAddress,
            onValueChange = { },
            readOnly = true,
            trailingIcon = Icons.Filled.AddLocationAlt,
            onTrailingIconClick = { newTaskViewModel.onOpenSearchLocationDialogButtonClick() },
            label = stringResource(R.string.new_task_delivery_address_label),
            modifier = Modifier.fillMaxWidth(),
            isError = newTaskViewModel.deliveryAddressError,
            errorMessage = stringResource(R.string.new_task_delivery_address_error_message)
        )

        /**
         * Products
         **/
        Text(
            text = stringResource(R.string.new_task_products_section_title),
            fontSize = dpToSp(R.dimen.new_task_products_section_title_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        ProductsList(newTaskViewModel = newTaskViewModel)
    }
}


@Composable
private fun ProductsList(
    modifier: Modifier = Modifier,
    newTaskViewModel: NewTaskViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.alert_dialog_content_vertical_space)),
        modifier = modifier
    ) {
        val products = newTaskViewModel.products

        if (products.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProductAddButton(newTaskViewModel = newTaskViewModel)

                Text(
                    text = stringResource(R.string.new_task_no_products_text),
                    fontSize = dpToSp(R.dimen.new_task_no_products_message_text_size)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.new_task_products_list_vertical_padding)),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                items(newTaskViewModel.products) { product ->
                    ProductContainer(
                        name = product.name,
                        amount = product.amount,
                        onDeleteClick = { newTaskViewModel.onProductDelete(product) }
                    )
                }

            }

            ProductAddButton(newTaskViewModel = newTaskViewModel)
        }

    }
}


@Composable
private fun ProductAddButton(
    newTaskViewModel: NewTaskViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AddCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .requiredSize(dimensionResource(R.dimen.new_task_product_add_button_icon_size))
                .bounceClick()
                .clickable { newTaskViewModel.onNewProductButtonClick() }
        )
    }
}


@Composable
private fun ProductContainer(
    name: String,
    amount: Int,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = name,
                fontSize = dpToSp(R.dimen.new_task_product_details_text_size),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.new_task_product_details_name_end_padding))
            )
            Text(
                text = "$amount pcs",
                fontWeight = FontWeight.Bold,
                fontSize = dpToSp(R.dimen.new_task_product_details_text_size),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Icon(
            imageVector = Icons.Outlined.DeleteOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .bounceClick()
                .requiredSize(dimensionResource(R.dimen.new_task_product_delete_icon_size))
                .clickable { onDeleteClick() }
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    )
}


@Composable
private fun TaskCreateButton(
    newTaskViewModel: NewTaskViewModel,
    modifier: Modifier = Modifier
) {
    AppButton(
        onClick = { newTaskViewModel.onTaskCreateButtonClick() },
        text = stringResource(R.string.new_task_create_task_button_text),
        icon = Icons.Filled.Add,
        modifier = modifier
    )
}


@Composable
private fun ShowProductAddAlertDialog(newTaskViewModel: NewTaskViewModel) {
    if (newTaskViewModel.showAddProductDialog)
        AddProductAlertDialog(newTaskViewModel = newTaskViewModel)
}


@Composable
private fun ShowLocationSearchDialog(newTaskViewModel: NewTaskViewModel) {
    if (newTaskViewModel.showLocationSearchDialog)
        LocationSearchAlertDialog(newTaskViewModel = newTaskViewModel)
}


@Composable
private fun ShowProductDeleteConfirmationDialog(
    newTaskViewModel: NewTaskViewModel,
) {
    if (newTaskViewModel.showDeleteConfirmationDialog)
        ProductDeleteConfirmationDialog(
            newTaskViewModel = newTaskViewModel,
            onYesClick = { newTaskViewModel.onProductDeleteConfirmationYesClick() },
            onNoClick = { newTaskViewModel.onProductDeleteConfirmationNoClick() }
        )
}