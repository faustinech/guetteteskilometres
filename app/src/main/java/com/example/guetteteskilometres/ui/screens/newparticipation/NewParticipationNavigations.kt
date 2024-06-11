package com.example.guetteteskilometres.ui.screens.newparticipation

data class NewParticipationNavigations(
    val navigateUp: () -> Unit,
    val navigateToNewPerson: (idEvent: Long) -> Unit
)