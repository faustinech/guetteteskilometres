package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Event
import kotlinx.coroutines.flow.Flow

interface EventStorage {
    fun getEvents(): Flow<List<Event>>

    suspend fun saveEvent(event: Event)

    suspend fun getEvent(id: Long): Event?

    suspend fun closeEvent(id: Long)
}