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
    data object SucessSave: Dialog
    data object ErrorSave: Dialog
}

enum class ParticipationColumns(val display: String, val inRecap: Boolean) {
    PRENOM("Prénom", inRecap = true),
    NOM("Nom", inRecap = true),
    DEBUT("Début", inRecap = false),
    FIN("Fin", inRecap = false),
    TOTAL("Total", inRecap = true)
}