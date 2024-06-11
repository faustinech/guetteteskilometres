package com.example.guetteteskilometres.ui.screens.newperson

sealed interface NewPersonEvents {
    data object NavigateUp: NewPersonEvents
    data class NavigateUpWithPerson(
        val idPerson: Long
    ): NewPersonEvents
}