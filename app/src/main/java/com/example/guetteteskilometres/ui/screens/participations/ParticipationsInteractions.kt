package com.example.guetteteskilometres.ui.screens.participations

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.ui.screens.participants.ParticipantField

data class ParticipationsInteractions(
    val onBackClicked: () -> Unit,
    val onCreationParticipationClicked: () -> Unit,
    val onParticipationClicked: (Long) -> Unit,
    val onFilterChanged: (String) -> Unit,
    val onConfirmClotureClicked: () -> Unit,
    val onDismissDialogClicked: () -> Unit,
    val onValidateNewParticipantClicked: () -> Unit,
    val onActiveChanged: (Boolean) -> Unit,
    val onNewParticipantFieldChanged: (ParticipantField, String?) -> Unit,
    val onNewParticipantsDismissClicked: () -> Unit,
    val onPersonChanged: (Person) -> Unit,
    val onAddPersonClicked: () -> Unit,
    val onValidateClicked: () -> Unit,
    val onFieldChanged: (ParticipationField, String?) -> Unit,
    val onDeleteParticipation: (Long) -> Unit
)