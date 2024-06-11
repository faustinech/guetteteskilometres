package com.example.guetteteskilometres.db.storage

import com.example.guetteteskilometres.data.datasource.EventStorage
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.db.dao.EventDao
import com.example.guetteteskilometres.db.mapping.toEntity
import com.example.guetteteskilometres.db.mapping.toModel

class RoomEventStorage(
    private val eventDao: EventDao
): EventStorage {

    override suspend fun getEvents(): List<Event> {
        return eventDao.getEvents().map { it.toModel() }
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
}