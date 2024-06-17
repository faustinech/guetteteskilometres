package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.ParticipationStorage
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ParticipationRepository(
    private val storage: ParticipationStorage
) {
    fun getParticipations(idEvent: Long): Flow<List<Participation>> {
        return storage.getParticipations(idEvent)
    }

    suspend fun saveParticipation(
        idParticipation: Long?,
        startMeters: Int,
        endMeters: Int?,
        person: Person,
        event: Event
    ) {
        withContext(Dispatchers.IO) {
            storage.saveParticipation(
                Participation(
                    id = idParticipation ?: 0,
                    person = person,
                    startMeters = startMeters,
                    endMeters = endMeters,
                    event = event
                )
            )
        }
    }

    suspend fun getParticipation(idParticipation: Long): Participation? {
        return withContext(Dispatchers.IO) {
            storage.getParticipation(idParticipation)
        }
    }

    suspend fun removeParticipation(idParticipation: Long) {
        withContext(Dispatchers.IO) {
            storage.removeParticipation(idParticipation)
        }
    }
}