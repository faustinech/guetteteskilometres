package com.example.guetteteskilometres.ui.screens.participations

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import kotlinx.collections.immutable.ImmutableList

data class ParticipationsState(
    val event: Event?,
    val participations: ImmutableList<Participation>,
    val filter: String,
    val dialog: Dialog
)

sealed interface Dialog {
    data object None: Dialog
    data class Input(
        val person: Person?,
        val startMeters: String?,
        val endMeters: String?,
        val persons: ImmutableList<Person>,
        val alert: SaveAlert? = null
    ): Dialog
}

enum class ParticipationField {
    StartMeters,
    EndMeters;
}