package com.example.guetteteskilometres.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.navigation.Home

fun NavGraphBuilder.home(
    navigations: HomeNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository
) {
    composable<Home> {
        val viewModel = HomeViewModel(
            eventRepository = eventRepository,
            participationRepository = participationRepository
        )
        HomeScreen(
            navigations = navigations,
            viewModel = viewModel
        )
    }
}