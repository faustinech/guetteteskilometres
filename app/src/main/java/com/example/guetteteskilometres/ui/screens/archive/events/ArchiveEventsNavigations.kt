package com.example.guetteteskilometres.ui.screens.archive.events

data class ArchiveEventsNavigations(
    val navigateUp: () -> Unit,
    val navigateToEvent: (Long) -> Unit
)