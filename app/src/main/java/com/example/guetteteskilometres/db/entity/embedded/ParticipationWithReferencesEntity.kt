package com.example.guetteteskilometres.db.entity.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.ParticipationEntity
import com.example.guetteteskilometres.db.entity.PersonEntity

data class ParticipationWithReferencesEntity(
    @Embedded
    val participation: ParticipationEntity,
    @Relation(parentColumn = "id_person", entityColumn = "id")
    val person: PersonEntity,
    @Relation(parentColumn = "id_event", entityColumn = "id")
    val event: EventEntity
)
