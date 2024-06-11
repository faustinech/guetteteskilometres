package com.example.guetteteskilometres.ui.screens.home

import com.example.guetteteskilometres.data.model.Event

data class HomeNavigations(
    val navigateToEvent: (Event) -> Unit,
    val navigateToNewEvent: () -> Unit
)