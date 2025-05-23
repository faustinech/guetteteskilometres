package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.guetteteskilometres.db.entity.ParticipationEntity
import com.example.guetteteskilometres.db.entity.embedded.ParticipationWithReferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipationDao {
    @Upsert
    suspend fun insertParticipation(participationEntity: ParticipationEntity): Long?

    @Update
    suspend fun updateParticipation(participationEntity: ParticipationEntity)

    @Query("delete from participation where id = :id")
    suspend fun removeParticipation(id: Long)

    @Query("select * from participation where id_event = :idEvent")
    fun getEventParticipations(idEvent: Long): Flow<List<ParticipationWithReferencesEntity>>

    @Query("select * from participation where id_event = :idEvent and id_person = :idPerson")
    suspend fun getPersonParticipations(idEvent: Long, idPerson: Long): List<ParticipationWithReferencesEntity>

    @Query("select * from participation where id = :idParticipation")
    suspend fun getParticipation(idParticipation: Long): ParticipationWithReferencesEntity?

    @Query("select * from participation where id_event = :idEvent order by id desc limit 1")
    suspend fun getLastParticipation(idEvent: Long): ParticipationWithReferencesEntity?

    @Query("select * from participation where id_event = :idEvent and id != :idParticipation order by id desc limit 1")
    suspend fun getLastParticipation(idEvent: Long, idParticipation: Long): ParticipationWithReferencesEntity?
}