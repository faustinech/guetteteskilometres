package com.example.guetteteskilometres.ui.screens.participants

data class ParticipantsNavigations(
    val navigateUp: () -> Unit,
    val navigateToPerson: (idPerson: Long) -> Unit,
    val navigateToNewPerson: () -> Unit
)