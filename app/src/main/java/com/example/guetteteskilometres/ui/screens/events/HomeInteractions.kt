package com.example.guetteteskilometres.ui.screens.events

data class HomeInteractions(
    val onEventsClicked: () -> Unit,
    val onClosedEventsClicked: () -> Unit,
    val onUsersClicked: () -> Unit
)