package com.example.guetteteskilometres.ui.screens.events

data class EventsNavigations(
    val navigateUp: () -> Unit,
    val navigateToEvent: (Long) -> Unit
)