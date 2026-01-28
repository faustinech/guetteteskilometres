package com.example.guetteteskilometres.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.guetteteskilometres.di.AppDependencies
import com.example.guetteteskilometres.ui.screens.events.EventsNavigations
import com.example.guetteteskilometres.ui.screens.events.events
import com.example.guetteteskilometres.ui.screens.home.HomeNavigations
import com.example.guetteteskilometres.ui.screens.home.home
import com.example.guetteteskilometres.ui.screens.newparticipation.NewParticipationNavigations
import com.example.guetteteskilometres.ui.screens.newparticipation.newParticipation
import com.example.guetteteskilometres.ui.screens.participants.ParticipantsNavigations
import com.example.guetteteskilometres.ui.screens.participants.participants
import com.example.guetteteskilometres.ui.screens.participations.ParticipationsNavigations
import com.example.guetteteskilometres.ui.screens.participations.participations
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
                navigateToEvents = {
                    navController.navigate(Events)
                },
                navigateToClosedEvents = {
                    // TODO FCH : revoir
                },
                navigateToUsers = {
                    navController.navigate(Participants)
                }
            )
        )
        participants(
            navigations = ParticipantsNavigations(
                navigateUp = navController::popBackStack
            ),
            personRepository = appDependencies.personRepository
        )
        events(
            navigations = EventsNavigations(
                navigateUp = navController::popBackStack,
                navigateToEvent = {
                    // TODO FCH : à remplir
                }
            ),
            eventRepository = appDependencies.eventRepository,
            participationRepository = appDependencies.participationRepository
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
    }

}