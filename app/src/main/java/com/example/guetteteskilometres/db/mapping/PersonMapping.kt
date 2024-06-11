package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.db.entity.PersonEntity

fun PersonEntity.toModel() = Person(
    id = id,
    name = name,
    firstname = firstname,
    idEvent = idEvent
)

fun Person.toEntity() = PersonEntity(
    id = id,
    name = name,
    firstname = firstname,
    idEvent = idEvent
)