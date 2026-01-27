package com.example.guetteteskilometres.ui.screens.participants

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import kotlinx.collections.immutable.ImmutableList

data class ParticipantsState(
    val persons: ImmutableList<Person>,
    val activeFilter: ParticipantFilter,
    val filter: String
)