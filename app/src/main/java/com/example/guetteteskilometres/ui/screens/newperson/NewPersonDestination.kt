package com.example.guetteteskilometres.ui.screens.newperson

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.guetteteskilometres.constants.GTKArguments
import com.example.guetteteskilometres.constants.GTKRoutes
import com.example.guetteteskilometres.data.repository.PersonRepository

fun NavGraphBuilder.newPerson(
    navigations: NewPersonNavigations,
    personRepository: PersonRepository
) {
    composable(
        GTKRoutes.newPerson,
        arguments = listOf(navArgument(GTKArguments.idEvent) { type = NavType.LongType })
    ) { backStackEntry ->
        val idEvent = backStackEntry.arguments?.getLong(GTKArguments.idEvent)
        val viewModel = NewPersonViewModel(
            personRepository = personRepository
        )
        NewPersonScreen(
            navigations = navigations,
            viewModel = viewModel, idEvent = idEvent
        )
    }
}