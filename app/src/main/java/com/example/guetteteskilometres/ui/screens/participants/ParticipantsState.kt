package com.example.guetteteskilometres.ui.screens.participants

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import kotlinx.collections.immutable.ImmutableList

data class ParticipantsState(
    val persons: ImmutableList<Person>,
    val activeFilter: ParticipantFilter,
    val filter: String,
    val alert: ParticipantsAlert? = null,
    val isDialogVisible: Boolean = false,
    val idPerson: Long? = null,
    val firstname: String? = null,
    val name: String? = null,
    val email: String? = null,
    val active: Boolean? = null
)

sealed interface ParticipantsAlert {
    data object Success: ParticipantsAlert
    data object MissingField: ParticipantsAlert
    data object Error: ParticipantsAlert
}