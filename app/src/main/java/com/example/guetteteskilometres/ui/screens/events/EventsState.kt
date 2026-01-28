package com.example.guetteteskilometres.ui.screens.events

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import kotlinx.collections.immutable.ImmutableList

data class EventsState(
    val events: ImmutableList<Event>,
    val dialog: Dialog,
    val idEventToDelete: Long?,
    val filter: String,
    val isEditDialogVisible: Boolean = false,
    val alert: SaveAlert? = null,
    val idEvent: Long? = null,
    val name: String? = null,
    val startMeters: String? = null,
    val active: Boolean? = null,
    val ascending: Boolean? = null
)

sealed interface Dialog {
    data object None: Dialog
    data class ConfirmSuppression(
        val libelle: String
    ): Dialog
}

enum class EventField {
    Name,
    StartMeters;
}