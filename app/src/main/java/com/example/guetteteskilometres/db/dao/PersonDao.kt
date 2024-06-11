package com.example.guetteteskilometres.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.guetteteskilometres.db.entity.PersonEntity

@Dao
interface PersonDao {
    @Insert
    suspend fun insertPerson(personEntity: PersonEntity): Long?

    @Query("select * from person where id_event = :id")
    suspend fun getEventPersons(id: Long): List<PersonEntity>

    @Query("select * from person where id = :idPerson")
    suspend fun getPerson(idPerson: Long): PersonEntity?
}