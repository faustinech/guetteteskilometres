package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.EventStorage
import com.example.guetteteskilometres.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository(
    private val storage: EventStorage
) {
    fun getEvents(active: Boolean = true): Flow<List<Event>> {
        return storage.getEvents(active)
    }

    suspend fun saveEvent(id: Long?, name: String, startMeters: String, active: Boolean?, ascending: Boolean?): Long? =
        withContext(Dispatchers.IO) {
            try {
                storage.saveEvent(
                    Event(
                        id = id ?: 0L,
                        name = name,
                        startMeters = startMeters.toInt(),
                        ascending = ascending ?: true,
                        active = active ?: true,
                        totalMeters = 0,
                        nbParticipants = null
                    )
                )
            } catch (_: NumberFormatException) {
                null
            }
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