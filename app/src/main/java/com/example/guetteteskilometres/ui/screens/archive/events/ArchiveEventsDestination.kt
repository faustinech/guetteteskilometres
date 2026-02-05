package com.example.guetteteskilometres.ui.screens.archive.events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.ui.navigation.ArchiveEvents

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.archiveEvents(
    navigations: ArchiveEventsNavigations,
    eventRepository: EventRepository
) {
    composable<ArchiveEvents> {
        val viewModel = viewModel {
            ArchiveEventsViewModel(
                eventRepository = eventRepository
            )
        }
        ArchiveEventsScreen(
            navigations = navigations,
            viewModel = viewModel
        )
    }
}