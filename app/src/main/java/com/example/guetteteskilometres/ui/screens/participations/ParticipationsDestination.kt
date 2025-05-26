package com.example.guetteteskilometres.ui.screens.participations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.navigation.Participations

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.participations(
    navigations: ParticipationsNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository
) {
    composable<Participations> { backStackEntry ->
        val arguments: Participations = backStackEntry.toRoute()
        val viewModel = viewModel {
            ParticipationsViewModel(
                eventRepository = eventRepository,
                participationRepository = participationRepository
            )
        }
        ParticipationsScreen(
            navigations = navigations,
            viewModel = viewModel,
            idEvent = arguments.idEvent
        )
    }
}