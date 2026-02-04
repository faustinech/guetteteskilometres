package com.example.guetteteskilometres.ui.screens.archive.participations

data class ArchiveParticipationsInteractions(
    val onBackClicked: () -> Unit,
    val onFilterChanged: (String) -> Unit
)