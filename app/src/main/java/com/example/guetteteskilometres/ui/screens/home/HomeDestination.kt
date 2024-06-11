package com.example.guetteteskilometres.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.constants.GTKRoutes
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository

fun NavGraphBuilder.home(
    navigations: HomeNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository
) {
    composable(GTKRoutes.events) {
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