package com.example.guetteteskilometres.ui.screens.events

import com.example.guetteteskilometres.data.model.Event

data class EventsInteractions(
    val onBackClicked: () -> Unit,
    val onEventClicked: (Long) -> Unit,
    val onNewEventClicked: () -> Unit,
    val onEditEventClicked: (Long) -> Unit,
    val onFilterChanged: (String) -> Unit,
    val onDeleteEventClicked: (Event) -> Unit,
    val onConfirmClicked: () -> Unit,
    val onDismissClicked: () -> Unit,
    val onValidateClicked: () -> Unit,
    val onActiveChanged: (Boolean) -> Unit,
    val onAscendingChanged: (Boolean) -> Unit,
    val onFieldChanged: (EventField, String?) -> Unit
)