package com.example.guetteteskilometres.db.storage

import com.example.guetteteskilometres.data.datasource.PersonStorage
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.db.dao.PersonDao
import com.example.guetteteskilometres.db.mapping.toEntity
import com.example.guetteteskilometres.db.mapping.toModel

class RoomPersonStorage(
    private val personDao: PersonDao
): PersonStorage {
    override suspend fun getPersons(idEvent: Long): List<Person> {
        return personDao.getEventPersons(idEvent).map { it.toModel() }
    }

    override suspend fun savePerson(person: Person): Long? {
        return personDao.insertPerson(person.toEntity())
    }

    override suspend fun getPerson(idPerson: Long): Person? {
        return personDao.getPerson(idPerson)?.toModel()
    }
}