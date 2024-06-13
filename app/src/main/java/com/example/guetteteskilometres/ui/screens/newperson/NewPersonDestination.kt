package com.example.guetteteskilometres.ui.screens.newperson

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.navigation.NewPerson

fun NavGraphBuilder.newPerson(
    navigations: NewPersonNavigations,
    personRepository: PersonRepository
) {
    composable<NewPerson> { backStackEntry ->
        val arguments: NewPerson = backStackEntry.toRoute()
        val viewModel = NewPersonViewModel(
            personRepository = personRepository
        )
        NewPersonScreen(
            navigations = navigations,
            viewModel = viewModel, idEvent = arguments.idEvent
        )
    }
}