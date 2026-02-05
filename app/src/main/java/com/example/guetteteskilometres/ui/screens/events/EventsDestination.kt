package com.example.guetteteskilometres.ui.screens.events

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.ui.navigation.Events

fun NavGraphBuilder.events(
    navigations: EventsNavigations,
    eventRepository: EventRepository
) {
    composable<Events> {
        val viewModel = viewModel {
            EventsViewModel(
                eventRepository = eventRepository
            )
        }
        EventsScreen(
            navigations = navigations,
            viewModel = viewModel
        )
    }
}