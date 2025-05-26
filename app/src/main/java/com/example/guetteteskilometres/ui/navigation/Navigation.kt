package com.example.guetteteskilometres.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appDependencies: AppDependencies
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        home(
            navigations = HomeNavigations(
                navigateToEvent = { event ->
                    navController.navigate(Participations(event.id))
                },
                navigateToNewEvent = {
                    navController.navigate(NewEvent)
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
                navigateToParticipation = { idEvent, idParticipation, isLastParticipation ->
                    navController.navigate(
                        NewParticipation(
                            idEvent = idEvent,
                            idParticipation = idParticipation ?: -1,
                            isLastParticipation = isLastParticipation
                        )
                    )
                }
            ),
            eventRepository = appDependencies.eventRepository,
            participationRepository = appDependencies.participationRepository
        )
        newParticipation(
            navigations = NewParticipationNavigations(
                navigateUp = navController::popBackStack,
                navigateToNewPerson = { idEvent ->
                    navController.navigate(NewPerson(idEvent))
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