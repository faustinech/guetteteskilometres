package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Participation
import kotlinx.coroutines.flow.Flow

interface ParticipationStorage {
    fun getParticipations(idEvent: Long): Flow<List<Participation>>

    suspend fun saveParticipation(participation: Participation)

    suspend fun getParticipation(idParticipation: Long): Participation?

    suspend fun removeParticipation(idParticipation: Long)

    suspend fun getLastParticipation(idEvent: Long, idParticipation: Long?): Participation?
}