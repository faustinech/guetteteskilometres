package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.db.entity.ParticipationEntity
import com.example.guetteteskilometres.db.entity.embedded.ParticipationWithReferencesEntity

fun ParticipationWithReferencesEntity.toModel() = Participation(
    id = participation.id,
    event = event.toModel(),
    person = person.toModel(),
    startMeters = participation.startMeters,
    endMeters = participation.endMeters
)

fun Participation.toEntity() = ParticipationEntity(
    id = id,
    idEvent = event.id,
    idPerson = person.id,
    startMeters = startMeters,
    endMeters = endMeters
)

// TODO : utiliser des WithReferences ?