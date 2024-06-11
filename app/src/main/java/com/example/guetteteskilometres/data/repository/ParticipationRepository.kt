package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.ParticipationStorage
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParticipationRepository(
    private val storage: ParticipationStorage
) {
    suspend fun getParticipations(idEvent: Long): List<Participation> {
        // TODO FCH : utiliser des flows ?
        return withContext(Dispatchers.IO) {
            storage.getParticipations(idEvent)
        }
    }

    suspend fun saveParticipation(startMeters: Int, endMeters: Int?, person: Person, event: Event) {
        withContext(Dispatchers.IO) {
            storage.saveParticipation(
                Participation(
                    id = 0,
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
}