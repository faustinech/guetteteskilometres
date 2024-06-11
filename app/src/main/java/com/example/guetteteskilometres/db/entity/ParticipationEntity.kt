package com.example.guetteteskilometres.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "participation",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_event"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_person"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParticipationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "id_event")
    val idEvent: Long,
    @ColumnInfo(name = "id_person")
    val idPerson: Long,
    @ColumnInfo(name = "start_meters")
    val startMeters: Int,
    @ColumnInfo(name = "end_meters")
    val endMeters: Int?
)
