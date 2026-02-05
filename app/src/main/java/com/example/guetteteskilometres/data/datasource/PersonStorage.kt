package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonStorage {
    suspend fun savePerson(person: Person): Long?

    suspend fun getPerson(idPerson: Long): Person?

    fun getAll(): Flow<List<Person>>
}