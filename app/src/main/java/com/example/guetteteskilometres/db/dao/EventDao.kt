package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.guetteteskilometres.db.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity): Long?

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("update event set is_done = 1 where id = :id")
    suspend fun closeEvent(id: Long)

    @Query("select * from event")
    fun getEvents(): Flow<List<EventEntity>>

    @Query("select * from event where id = :id")
    suspend fun getEvent(id: Long): EventEntity?
}