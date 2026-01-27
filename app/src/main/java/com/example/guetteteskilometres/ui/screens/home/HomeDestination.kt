package com.example.guetteteskilometres.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.ui.navigation.Home

fun NavGraphBuilder.home(
    navigations: HomeNavigations
) {
    composable<Home> {
        // val viewModel = viewModel { HomeViewModel() }
        HomeScreen(
            navigations = navigations
        )
    }
}