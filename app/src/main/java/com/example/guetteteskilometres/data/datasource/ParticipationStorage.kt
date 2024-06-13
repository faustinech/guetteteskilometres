package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Participation

interface ParticipationStorage {
    suspend fun getParticipations(idEvent: Long): List<Participation>

    suspend fun saveParticipation(participation: Participation)

    suspend fun getParticipation(idParticipation: Long): Participation?

    suspend fun removeParticipation(idParticipation: Long)
}