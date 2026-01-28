package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.guetteteskilometres.db.entity.EventEntity
import com.example.guetteteskilometres.db.entity.embedded.EventWithReferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Upsert
    suspend fun insertEvent(event: EventEntity): Long?

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("update event set active = 0 where id = :id")
    suspend fun closeEvent(id: Long)

    @Query("select * from event where active = :active")
    fun getEvents(active: Boolean): Flow<List<EventWithReferencesEntity>>

    @Query("select * from event where id = :id")
    suspend fun getEvent(id: Long): EventEntity?

    @Query("delete from event where id = :id")
    suspend fun deleteEvent(id: Long)
}