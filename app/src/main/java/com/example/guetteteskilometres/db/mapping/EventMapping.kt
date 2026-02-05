package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.embedded.EventWithReferencesEntity

fun EventEntity.toModel() = Event(
    id = id,
    name = name,
    startMeters = startMeters,
    active = active,
    ascending = ascending,
    totalMeters = null,
    nbParticipants = null
)

fun EventWithReferencesEntity.toModel() = Event(
    id = event.id,
    name = event.name,
    startMeters = event.startMeters,
    active = event.active,
    ascending = event.ascending,
    participations = participations.map { it.toModel() },
    nbParticipants = null,
    totalMeters = null
)

fun Event.toEntity() = EventEntity(
    id = id,
    name = name,
    startMeters = startMeters,
    active = active,
    ascending = ascending
)