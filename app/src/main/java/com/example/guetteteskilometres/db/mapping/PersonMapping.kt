package com.example.guetteteskilometres.db.mapping

import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.db.entity.PersonEntity

fun PersonEntity.toModel() = Person(
    id = id,
    name = name,
    firstname = firstname,
    email = email,
    active = active
)

fun Person.toEntity() = PersonEntity(
    id = id,
    name = name,
    firstname = firstname,
    email = email,
    active = active
)