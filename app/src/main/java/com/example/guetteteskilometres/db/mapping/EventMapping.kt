package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.embedded.EventWithReferencesEntity

fun EventEntity.toModel() = Event(
    id = id,
    name = name,
    totalMeters = null,
    isDone = isDone,
    nbParticipants = null
)

fun EventWithReferencesEntity.toModel() = Event(
    id = event.id,
    name = event.name,
    isDone = event.isDone,
    participations = participations.map { it.toModel() },
    nbParticipants = null,
    totalMeters = null
)

fun Event.toEntity() = EventEntity(
    id = id,
    name = name,
    isDone = isDone
)