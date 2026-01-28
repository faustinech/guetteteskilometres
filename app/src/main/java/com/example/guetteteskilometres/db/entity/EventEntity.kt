package com.example.guetteteskilometres.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "event"
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "start_meters")
    val startMeters: Int,
    @ColumnInfo(name = "ascending")
    val ascending: Boolean,
    @ColumnInfo(name = "active")
    val active: Boolean
)
