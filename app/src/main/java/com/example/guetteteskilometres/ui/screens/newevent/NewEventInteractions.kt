package com.example.guetteteskilometres.ui.screens.newevent

data class NewEventInteractions(
    val onNameChanged: (String) -> Unit,
    val onBackClicked: () -> Unit,
    val onValidateClicked: () -> Unit
)