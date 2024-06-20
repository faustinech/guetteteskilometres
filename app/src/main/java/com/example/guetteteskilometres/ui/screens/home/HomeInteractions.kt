package com.example.guetteteskilometres.ui.screens.home

import com.example.guetteteskilometres.data.model.Event

data class HomeInteractions(
    val onEventClicked: (Event) -> Unit,
    val onAddEventClicked: () -> Unit,
    val onEventLongClicked: (Event) -> Unit,
    val onDismissDialog: () -> Unit,
    val onConfirmDialog: () -> Unit
)