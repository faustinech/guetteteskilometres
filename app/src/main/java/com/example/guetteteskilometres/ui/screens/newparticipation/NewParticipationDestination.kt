package com.example.guetteteskilometres.ui.screens.newparticipation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.guetteteskilometres.constants.GTKArguments
import com.example.guetteteskilometres.constants.GTKRoutes
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.data.repository.PersonRepository

fun NavGraphBuilder.newParticipation(
    navigations: NewParticipationNavigations,
    eventRepository: EventRepository,
    participationRepository: ParticipationRepository,
    personRepository: PersonRepository
) {
    composable(
        GTKRoutes.newParticipation,
        arguments = listOf(navArgument(GTKArguments.idEvent) { type = NavType.LongType })
    ) { backStackEntry ->
        val idEvent = backStackEntry.arguments?.getLong(GTKArguments.idEvent)
        val idParticipation = backStackEntry.arguments?.getString(GTKArguments.idParticipation)?.toLong()
        val idPerson = backStackEntry.savedStateHandle.get<String>("ID_PERSON")?.toLong()
        backStackEntry.savedStateHandle.remove<String>("ID_PERSON")
        val viewModel = NewParticipationViewModel(
            eventRepository = eventRepository,
            participationRepository = participationRepository,
            personRepository = personRepository
        )
        NewParticipationScreen(
            navigations = navigations,
            viewModel = viewModel,
            idEvent = idEvent,
            idParticipation = idParticipation,
            idPerson = idPerson
        )
    }
}