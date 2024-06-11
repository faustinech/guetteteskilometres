package com.example.guetteteskilometres.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "person",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_event"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "id_event")
    val idEvent: Long,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "firstname")
    val firstname: String
)
