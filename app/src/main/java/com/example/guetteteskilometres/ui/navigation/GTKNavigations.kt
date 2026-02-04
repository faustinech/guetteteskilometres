package com.example.guetteteskilometres.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object Events

@Serializable
data object Participants

@Serializable
data object ArchiveEvents

@Serializable
data class ArchiveParticipations(
    val idEvent: Long
)

@Serializable
data class Participations(
    val idEvent: Long
)