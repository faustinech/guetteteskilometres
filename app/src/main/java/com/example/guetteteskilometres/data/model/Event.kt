package com.example.guetteteskilometres.data.model

data class Event(
    val id: Long,
    val name: String,
    val isDone: Boolean,
    var totalMeters: Int?,
    var nbParticipants: Int?,
    val participations: List<Participation> = emptyList()
)
