package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.guetteteskilometres.db.entity.ParticipationEntity
import com.example.guetteteskilometres.db.entity.embedded.ParticipationWithReferencesEntity

@Dao
interface ParticipationDao {
    @Insert
    suspend fun insertParticipation(participationEntity: ParticipationEntity): Long?

    @Update
    suspend fun updateParticipation(participationEntity: ParticipationEntity)

    @Query("delete from participation where id = :id")
    suspend fun removeParticipation(id: Long)

    @Query("select * from participation where id_event = :idEvent")
    suspend fun getEventParticipations(idEvent: Long): List<ParticipationWithReferencesEntity>

    @Query("select * from participation where id_event = :idEvent and id_person = :idPerson")
    suspend fun getPersonParticipations(idEvent: Long, idPerson: Long): List<ParticipationWithReferencesEntity>

    @Query("select * from participation where id = :idParticipation")
    suspend fun getParticipation(idParticipation: Long): ParticipationWithReferencesEntity?
}