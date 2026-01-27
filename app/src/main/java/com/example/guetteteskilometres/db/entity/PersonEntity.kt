package com.example.guetteteskilometres.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "person"
)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "firstname")
    val firstname: String,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "active")
    val active: Boolean
)
