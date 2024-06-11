package com.example.guetteteskilometres.ui.screens.participations

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import kotlinx.collections.immutable.ImmutableList

data class ParticipationsState(
    val event: Event?,
    val participations: ImmutableList<Participation>,
    val filter: String?,
    val dialog: Dialog
)

sealed interface Dialog {
    data object None: Dialog
    data object ConfirmCloture: Dialog
}