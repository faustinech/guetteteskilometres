package com.example.guetteteskilometres.ui.screens.home

import com.example.guetteteskilometres.data.model.Event
import kotlinx.collections.immutable.ImmutableList

data class HomeState(
    val events: ImmutableList<Event>,
    val dialog: Dialog,
    val idEventToDelete: Long?
)

sealed interface Dialog {
    data object None: Dialog
    data class ConfirmSuppression(
        val libelle: String
    ): Dialog
}