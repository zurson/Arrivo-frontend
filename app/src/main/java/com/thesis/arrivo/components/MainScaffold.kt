package com.thesis.arrivo.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun MainScaffold(
    mainScaffoldViewModel: MainScaffoldViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (mainScaffoldViewModel.showNavbar)
                NavBar(mainScaffoldViewModel)
        }
    ) { contentPadding ->
        content(contentPadding)
    }
}