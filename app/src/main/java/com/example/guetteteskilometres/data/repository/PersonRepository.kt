package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.PersonStorage
import com.example.guetteteskilometres.data.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(
    private val storage: PersonStorage
) {

    suspend fun savePerson(id: Long?, firstname: String, name: String?, email: String?, active: Boolean?): Long? {
        return withContext(Dispatchers.IO) {
            storage.savePerson(
                Person(
                    id = id ?: 0,
                    name = name,
                    firstname = firstname,
                    email = email,
                    active = active ?: true
                )
            )
        }
    }

    suspend fun getPerson(idPerson: Long): Person? {
        return withContext(Dispatchers.IO) {
            storage.getPerson(idPerson)
        }
    }

    fun getAll(): Flow<List<Person>> {
        return storage.getAll()
    }
}