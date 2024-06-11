package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.db.entity.EventEntity

fun EventEntity.toModel() = Event(
    id = id,
    name = name,
    totalMeters = null,
    isDone = isDone,
    nbParticipants = null
)

fun Event.toEntity() = EventEntity(
    id = id,
    name = name,
    isDone = isDone
)