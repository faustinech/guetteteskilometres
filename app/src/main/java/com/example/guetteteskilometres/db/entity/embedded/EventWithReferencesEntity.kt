package com.example.guetteteskilometres.db.entity.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.ParticipationEntity

data class EventWithReferencesEntity(
    @Embedded
    val event: EventEntity,
    @Relation(parentColumn = "id", entityColumn = "id_event", entity = ParticipationEntity::class)
    val participations: List<ParticipationWithReferencesEntity>
)