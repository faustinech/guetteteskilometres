package com.example.guetteteskilometres.data.model

data class Event(
    val id: Long,
    val name: String,
    val startMeters: Int,
    val ascending: Boolean,
    val active: Boolean,
    var totalMeters: Int?,
    var nbParticipants: Int?,
    val participations: List<Participation> = emptyList()
)
