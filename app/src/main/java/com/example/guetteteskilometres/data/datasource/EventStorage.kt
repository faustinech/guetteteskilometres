package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Event

interface EventStorage {
    suspend fun getEvents(): List<Event>

    suspend fun saveEvent(event: Event)

    suspend fun getEvent(id: Long): Event?

    suspend fun closeEvent(id: Long)
}