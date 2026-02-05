package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.guetteteskilometres.db.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Upsert
    suspend fun insertPerson(personEntity: PersonEntity): Long?

    @Query("select * from person where id = :idPerson")
    suspend fun getPerson(idPerson: Long): PersonEntity?

    @Query("select * from person")
    fun getAll(): Flow<List<PersonEntity>>
}