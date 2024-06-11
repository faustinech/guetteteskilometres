package com.example.guetteteskilometres.ui.screens.newperson

data class NewPersonInteractions(
    val onNameChanged: (String) -> Unit,
    val onFirstNameChanged: (String) -> Unit,
    val onValidateClicked: () -> Unit,
    val onBackClicked: () -> Unit
)