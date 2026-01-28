package com.example.guetteteskilometres.ui.screens.participants

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import kotlinx.collections.immutable.ImmutableList

data class ParticipantsState(
    val persons: ImmutableList<Person>,
    val activeFilter: ParticipantFilter,
    val filter: String,
    val alert: SaveAlert? = null,
    val isDialogVisible: Boolean = false,
    val idPerson: Long? = null,
    val firstname: String? = null,
    val name: String? = null,
    val email: String? = null,
    val active: Boolean? = null
)

enum class ParticipantField {
    Firstname,
    Name,
    Email;
}