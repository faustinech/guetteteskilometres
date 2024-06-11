package com.example.guetteteskilometres.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guetteteskilometres.db.dao.EventDao
import com.example.guetteteskilometres.db.dao.ParticipationDao
import com.example.guetteteskilometres.db.dao.PersonDao
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.ParticipationEntity
import com.example.guetteteskilometres.db.entity.PersonEntity

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "GuetteTesKilometres.db"

@Database(
    entities = [
        EventEntity::class,
        ParticipationEntity::class,
        PersonEntity::class
    ],
    version = DATABASE_VERSION
)
abstract class GTKDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun participationDao(): ParticipationDao
    abstract fun personDao(): PersonDao
}