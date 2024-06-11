package com.example.guetteteskilometres.ui.screens.newparticipation

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Person
import kotlinx.collections.immutable.ImmutableList

data class NewParticipationState(
    val person: Person?,
    val event: Event?,
    val persons: ImmutableList<Person>,
    val startMeters: Int?,
    val endMeters: Int?,
    val idPersonErrorMessage: Int?,
    val idStartErrorMessage: Int?,
    val idEndErrorMessage: Int?
)