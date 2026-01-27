package com.example.guetteteskilometres.ui.screens.participants

import com.example.guetteteskilometres.data.model.enums.ParticipantFilter

data class ParticipantsInteractions(
    val onBackClicked: () -> Unit,
    val onPersonClicked: (Long) -> Unit,
    val onNewPersonClicked: () -> Unit,
    val onFilterChanged: (String) -> Unit,
    val onFilterSelected: (ParticipantFilter) -> Unit
)