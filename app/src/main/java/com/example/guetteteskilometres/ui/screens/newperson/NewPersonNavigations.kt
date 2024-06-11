package com.example.guetteteskilometres.ui.screens.newperson

data class NewPersonNavigations(
    val navigateUp: () -> Unit,
    val navigateUpWithPerson: (idPerson: Long) -> Unit
)