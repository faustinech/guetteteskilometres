package com.example.guetteteskilometres.ui.screens.archive.events

import com.example.guetteteskilometres.data.model.Event
import kotlinx.collections.immutable.ImmutableList

data class ArchiveEventsState(
    val events: ImmutableList<Event>,
    val filter: String,
    val dialog: Dialog
)

sealed interface Dialog {
    data object None: Dialog
    data object SuccessSave: Dialog
    data object ErrorSave: Dialog
}