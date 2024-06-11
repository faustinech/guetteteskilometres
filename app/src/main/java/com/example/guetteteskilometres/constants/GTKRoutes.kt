package com.example.guetteteskilometres.constants

object GTKRoutes {
    const val events = "events"
    const val newEvent = "new_event"
    const val participations = "participations/{id_event}"
    const val newParticipation = "add/participation/{id_event}"
    const val editParticipation = "edit/participation/{id_event}/{id_participation}"
    const val newPerson = "add/person/{id_event}"
}

object GTKArguments {
    const val idEvent = "id_event"
    const val idParticipation = "id_participation"
}