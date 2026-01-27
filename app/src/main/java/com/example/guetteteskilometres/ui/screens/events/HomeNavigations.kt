package com.example.guetteteskilometres.ui.screens.events

data class HomeNavigations(
    val navigateToEvents: () -> Unit,
    val navigateToClosedEvents: () -> Unit,
    val navigateToUsers: () -> Unit
)