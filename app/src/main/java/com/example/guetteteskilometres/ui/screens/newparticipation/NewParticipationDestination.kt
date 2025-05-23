package com.example.guetteteskilometres.ui.screens.newparticipation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.navigation.NewParticipation

fun NavGraphBuilder.newParticipation(
    navigations: NewParticipationNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository,
    personRepository: PersonRepository
) {
    composable<NewParticipation> { backStackEntry ->
        val arguments: NewParticipation = backStackEntry.toRoute()
        // TODO FCH : retour
        val idPerson = backStackEntry.savedStateHandle.get<String>("ID_PERSON")?.toLong()
        backStackEntry.savedStateHandle.remove<String>("ID_PERSON")
        val viewModel = viewModel {
            NewParticipationViewModel(
                eventRepository = eventRepository,
                participationRepository = participationRepository,
                personRepository = personRepository
            )
        }
        NewParticipationScreen(
            navigations = navigations,
            viewModel = viewModel,
            idEvent = arguments.idEvent,
            idParticipation = arguments.idParticipation,
            idPerson = idPerson,
            isLastParticipation = arguments.isLastParticipation
        )
    }
}