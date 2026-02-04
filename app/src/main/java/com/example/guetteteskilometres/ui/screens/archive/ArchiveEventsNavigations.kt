package com.example.guetteteskilometres.ui.screens.archive

data class ArchiveEventsNavigations(
    val navigateUp: () -> Unit,
    val navigateToEvent: (Long) -> Unit
)