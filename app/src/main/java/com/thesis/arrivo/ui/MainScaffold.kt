package com.thesis.arrivo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thesis.arrivo.components.navigation.NavBar
import com.thesis.arrivo.view_models.MainViewModel

@Composable
fun MainScaffold(
    mainViewModel: MainViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (mainViewModel.showNavbar)
                NavBar(mainViewModel)
        }
    ) { contentPadding ->
        content(contentPadding)
    }
}