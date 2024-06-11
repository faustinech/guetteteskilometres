package com.example.guetteteskilometres.data.datasource

import com.example.guetteteskilometres.data.model.Person

interface PersonStorage {
    suspend fun getPersons(idEvent: Long): List<Person> // TODO : rendre non suspend ?

    suspend fun savePerson(person: Person): Long?

    suspend fun getPerson(idPerson: Long): Person?
}