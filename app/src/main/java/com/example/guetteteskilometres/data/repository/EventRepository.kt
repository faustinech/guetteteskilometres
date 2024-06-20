package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.EventStorage
import com.example.guetteteskilometres.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository(
    private val storage: EventStorage
) {
    fun getEvents(): Flow<List<Event>> {
        return storage.getEvents()
    }

    suspend fun saveEvent(name: String) =
        withContext(Dispatchers.IO) {
            storage.saveEvent(
                Event(
                    id = 0,
                    name = name,
                    isDone = false,
                    totalMeters = 0,
                    nbParticipants = null
                )
            )
        }

    suspend fun getEvent(id: Long): Event? {
        return withContext(Dispatchers.IO) {
            storage.getEvent(id)
        }
    }

    suspend fun closeEvent(id: Long) {
        withContext(Dispatchers.IO) {
            storage.closeEvent(id)
        }
    }

    suspend fun deleteEvent(id: Long) {
        withContext(Dispatchers.IO) {
            storage.deleteEvent(id)
        }
    }
}