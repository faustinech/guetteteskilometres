package com.example.guetteteskilometres.data.model.enums

enum class ParticipationColumns(val display: String, val inRecap: Boolean) {
    PRENOM("Prénom", inRecap = true),
    NOM("Nom", inRecap = true),
    DEBUT("Début", inRecap = false),
    FIN("Fin", inRecap = false),
    TOTAL("Total", inRecap = true)
}