package com.example.guetteteskilometres.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.guetteteskilometres.constants.GTKArguments
import com.example.guetteteskilometres.constants.GTKRoutes
import com.example.guetteteskilometres.di.AppDependencies
import com.example.guetteteskilometres.ui.screens.home.HomeNavigations
import com.example.guetteteskilometres.ui.screens.home.home
import com.example.guetteteskilometres.ui.screens.newevent.NewEventNavigations
import com.example.guetteteskilometres.ui.screens.newparticipation.NewParticipationNavigations
import com.example.guetteteskilometres.ui.screens.newparticipation.newParticipation
import com.example.guetteteskilometres.ui.screens.newperson.NewPersonNavigations
import com.example.guetteteskilometres.ui.screens.newperson.newPerson
import com.example.guetteteskilometres.ui.screens.participations.ParticipationsNavigations
import com.example.guetteteskilometres.ui.screens.participations.participations
import newEvent

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appDependencies: AppDependencies
) {
    NavHost(
        navController = navController,
        startDestination = GTKRoutes.events,
        modifier = modifier
    ) {
        home(
            navigations = HomeNavigations(
                navigateToEvent = { event ->
                    val url = GTKRoutes.participations.replace("{${GTKArguments.idEvent}}", event.id.toString())
                    navController.navigate(url)
                },
                navigateToNewEvent = {
                    val url = GTKRoutes.newEvent
                    navController.navigate(url)
                }
            ),
            eventRepository = appDependencies.eventRepository,
            participationRepository = appDependencies.participationRepository
        )
        newEvent(
            navigations = NewEventNavigations(
                navigateUp = navController::popBackStack
            ),
            eventRepository = appDependencies.eventRepository
        )
        participations(
            navigations = ParticipationsNavigations(
                navigateUp = navController::popBackStack,
                navigateToParticipation = { idEvent, idParticipation ->
                    val url = GTKRoutes.editParticipation
                        .replace("{${GTKArguments.idEvent}}", idEvent.toString())
                        .replace(GTKArguments.idParticipation, idParticipation?.toString().orEmpty())
                    navController.navigate(url)
                },
                navigateToNewParticipation = { idEvent ->
                    val url = GTKRoutes.newParticipation
                        .replace("{${GTKArguments.idEvent}}", idEvent.toString())
                    navController.navigate(url)
                }
            ),
            eventRepository = appDependencies.eventRepository,
            participationRepository = appDependencies.participationRepository
        )
        newParticipation(
            navigations = NewParticipationNavigations(
                navigateUp = navController::popBackStack,
                navigateToNewPerson = { idEvent ->
                    val url = GTKRoutes.newPerson
                        .replace("{${GTKArguments.idEvent}}", idEvent.toString())
                    navController.navigate(url)
                }
            ),
            eventRepository = appDependencies.eventRepository,
            participationRepository = appDependencies.participationRepository,
            personRepository = appDependencies.personRepository
        )
        newPerson(
            navigations = NewPersonNavigations(
                navigateUp = navController::popBackStack,
                navigateUpWithPerson = { idPerson ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("ID_PERSON",
                        idPerson.toString()
                    )
                    navController.popBackStack()
                }
            ),
            personRepository = appDependencies.personRepository
        )
    }

}