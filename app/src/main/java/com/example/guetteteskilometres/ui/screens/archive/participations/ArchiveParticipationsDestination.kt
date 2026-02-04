package com.example.guetteteskilometres.ui.screens.archive.participations

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.navigation.ArchiveParticipations

fun NavGraphBuilder.archiveParticipations(
    navigations: ArchiveParticipationsNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository
) {
    composable<ArchiveParticipations> { backStackEntry ->
        val arguments: ArchiveParticipations = backStackEntry.toRoute()
        val viewModel = viewModel {
            ArchiveParticipationsViewModel(
                eventRepository = eventRepository,
                participationRepository = participationRepository
            )
        }
        ArchiveParticipationsScreen(
            navigations = navigations,
            viewModel = viewModel,
            idEvent = arguments.idEvent
        )
    }
}