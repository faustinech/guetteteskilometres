package com.example.guetteteskilometres.ui.screens.participants

sealed interface ParticipantsEvents {
    data object CreateParticipant: ParticipantsEvents
    data class EditParticipant(val id: Long): ParticipantsEvents
}