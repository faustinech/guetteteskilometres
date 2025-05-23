package com.example.guetteteskilometres.ui.screens.newparticipation

import com.example.guetteteskilometres.data.model.Person

data class NewParticipationInteractions(
    val onPersonChanged: (Person) -> Unit,
    val onStartMetersChanged: (String) -> Unit,
    val onEndMetersChanged: (String) -> Unit,
    val onAddPersonClicked: () -> Unit,
    val onValidationClicked: () -> Unit,
    val onBackClicked: () -> Unit,
    val onDeleteParticipationClicked: () -> Unit,
    val onDeleteConfirmationClicked: () -> Unit,
    val onDeleteDismissClicked: () -> Unit,
    val onClearStartMetersClicked: () -> Unit,
    val onClearEndMetersClicked: () -> Unit
)