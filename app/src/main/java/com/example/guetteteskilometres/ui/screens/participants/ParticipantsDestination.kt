package com.example.guetteteskilometres.ui.screens.participants

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.navigation.Participants

fun NavGraphBuilder.participants(
    navigations: ParticipantsNavigations,
    personRepository: PersonRepository
) {
    composable<Participants> {
        val viewModel = viewModel {
            ParticipantsViewModel(
                personRepository = personRepository
            )
        }
        ParticipantsScreen(
            navigations = navigations,
            viewModel = viewModel
        )
    }
}