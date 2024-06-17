package com.example.guetteteskilometres.data.repository

import com.example.guetteteskilometres.data.datasource.PersonStorage
import com.example.guetteteskilometres.data.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(
    private val storage: PersonStorage
) {
    fun getPersons(idEvent: Long): Flow<List<Person>> {
        return storage.getPersons(idEvent)
    }

    suspend fun savePerson(name: String?, firstname: String, idEvent: Long): Long? {
        return withContext(Dispatchers.IO) {
            storage.savePerson(
                Person(
                    id = 0,
                    name = name,
                    firstname = firstname,
                    idEvent = idEvent
                )
            )
        }
    }

    suspend fun getPerson(idPerson: Long): Person? {
        return withContext(Dispatchers.IO) {
            storage.getPerson(idPerson)
        }
    }
}