package com.example.guetteteskilometres.ui.navigation

import androidx.annotation.Nullable
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object NewEvent

@Serializable
data class Participations(
    val idEvent: Long
)

@Serializable
data class NewParticipation(
    val idEvent: Long,
    val idParticipation: Long,
    val isLastParticipation: Boolean
)

@Serializable
data class NewPerson(val idEvent: Long)