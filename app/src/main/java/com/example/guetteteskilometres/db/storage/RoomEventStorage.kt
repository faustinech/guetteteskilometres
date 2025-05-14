package com.example.guetteteskilometres.db.storage

import com.example.guetteteskilometres.data.datasource.EventStorage
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.db.dao.EventDao
import com.example.guetteteskilometres.db.dao.ParticipationDao
import com.example.guetteteskilometres.db.mapping.toEntity
import com.example.guetteteskilometres.db.mapping.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEventStorage(
    private val eventDao: EventDao
): EventStorage {

    override fun getEvents(): Flow<List<Event>> {
        return eventDao.getEvents().map { events -> events.map { it.toModel() } }
    }

    override suspend fun saveEvent(event: Event) {
        eventDao.insertEvent(event.toEntity())
    }

    override suspend fun getEvent(id: Long): Event? {
        return eventDao.getEvent(id)?.toModel()
    }

    override suspend fun closeEvent(id: Long) {
        eventDao.closeEvent(id)
    }

    override suspend fun deleteEvent(id: Long) {
        eventDao.deleteEvent(id)
    }
}