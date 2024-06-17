package com.example.guetteteskilometres.db.storage

import com.example.guetteteskilometres.data.datasource.PersonStorage
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.db.dao.PersonDao
import com.example.guetteteskilometres.db.mapping.toEntity
import com.example.guetteteskilometres.db.mapping.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomPersonStorage(
    private val personDao: PersonDao
): PersonStorage {
    override fun getPersons(idEvent: Long): Flow<List<Person>> {
        return personDao.getEventPersons(idEvent).map { persons -> persons.map { it.toModel() } }
    }

    override suspend fun savePerson(person: Person): Long? {
        return personDao.insertPerson(person.toEntity())
    }

    override suspend fun getPerson(idPerson: Long): Person? {
        return personDao.getPerson(idPerson)?.toModel()
    }
}