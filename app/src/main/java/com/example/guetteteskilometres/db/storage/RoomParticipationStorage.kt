package com.example.guetteteskilometres.db.storage

import com.example.guetteteskilometres.data.datasource.ParticipationStorage
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.db.dao.ParticipationDao
import com.example.guetteteskilometres.db.mapping.toEntity
import com.example.guetteteskilometres.db.mapping.toModel

class RoomParticipationStorage(
    private val participationDao: ParticipationDao
): ParticipationStorage {
    override suspend fun getParticipations(idEvent: Long): List<Participation> {
        return participationDao.getEventParticipations(idEvent).map { it.toModel() }
    }

    override suspend fun saveParticipation(participation: Participation) {
        participationDao.insertParticipation(participation.toEntity())
    }

    override suspend fun getParticipation(idParticipation: Long): Participation? {
        return participationDao.getParticipation(idParticipation)?.toModel()
    }
}