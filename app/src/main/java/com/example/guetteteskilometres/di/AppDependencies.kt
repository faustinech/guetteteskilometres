package com.example.guetteteskilometres.di

import android.content.Context
import androidx.room.Room
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.db.DATABASE_NAME
import com.example.guetteteskilometres.db.GTKDatabase
import com.example.guetteteskilometres.db.storage.RoomEventStorage
import com.example.guetteteskilometres.db.storage.RoomParticipationStorage
import com.example.guetteteskilometres.db.storage.RoomPersonStorage

class AppDependencies(context: Context) {

    private val roomDatabase: GTKDatabase = Room
        .databaseBuilder(context = context, klass = GTKDatabase::class.java, name = DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()


    private val eventStorage = RoomEventStorage(
        roomDatabase.eventDao()
    )

    private val participationStorage = RoomParticipationStorage(
        roomDatabase.participationDao()
    )

    private val personStorage = RoomPersonStorage(
        roomDatabase.personDao()
    )

    val eventRepository = EventRepository(
        storage = eventStorage
    )

    val participationRepository = ParticipationRepository(
        storage = participationStorage
    )

    val personRepository = PersonRepository(
        storage = personStorage
    )
}