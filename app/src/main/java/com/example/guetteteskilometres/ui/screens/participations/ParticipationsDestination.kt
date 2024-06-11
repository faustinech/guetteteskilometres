package com.example.guetteteskilometres.ui.screens.participations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.guetteteskilometres.constants.GTKArguments
import com.example.guetteteskilometres.constants.GTKRoutes
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository

fun NavGraphBuilder.participations(
    navigations: ParticipationsNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository
) {
    composable(
        GTKRoutes.participations,
        arguments = listOf(navArgument(GTKArguments.idEvent) { type = NavType.LongType })
    ) { backStackEntry ->
        val idEvent = backStackEntry.arguments?.getLong(GTKArguments.idEvent)
        val viewModel = ParticipationsViewModel(
            eventRepository = eventRepository,
            participationRepository = participationRepository
        )
        ParticipationsScreen(
            navigations = navigations,
            viewModel = viewModel,
            idEvent = idEvent
        )
    }
}