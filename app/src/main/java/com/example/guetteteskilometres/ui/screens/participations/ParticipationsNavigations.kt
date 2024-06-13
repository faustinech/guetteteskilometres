package com.example.guetteteskilometres.ui.screens.participations

data class ParticipationsNavigations(
    val navigateUp: () -> Unit,
    val navigateToParticipation: (idEvent: Long, idParticipation: Long?) -> Unit
)