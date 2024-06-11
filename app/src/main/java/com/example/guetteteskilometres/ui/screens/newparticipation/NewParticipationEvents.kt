package com.example.guetteteskilometres.ui.screens.newparticipation

sealed interface NewParticipationEvents {
    data object NavigateUp: NewParticipationEvents
    //data object NavigateToNewPerson: NewParticipationEvents
}