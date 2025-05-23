package com.example.guetteteskilometres.ui.screens.participations

data class ParticipationsInteractions(
    val onBackClicked: () -> Unit,
    val onCreationParticipationClicked: () -> Unit,
    val onParticipationClicked: (Long) -> Unit,
    val onClotureEventClicked: () -> Unit,
    val onFilterChanged: (String) -> Unit,
    val onConfirmClotureClicked: () -> Unit,
    val onDismissDialogClicked: () -> Unit,
    val onSaveClicked: () -> Unit
)