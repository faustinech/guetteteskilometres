package com.example.guetteteskilometres.ui.screens.archive

import com.example.guetteteskilometres.data.model.Event
import kotlinx.collections.immutable.ImmutableList

data class ArchiveEventsState(
    val events: ImmutableList<Event>,
    val filter: String,
    val dialog: Dialog
)

sealed interface Dialog {
    data object None: Dialog
    // TODO FCH : ajouter le message en cas d'échec/réussite de l'export des données
}