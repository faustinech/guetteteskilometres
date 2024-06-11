package com.example.guetteteskilometres.data.model

import kotlin.math.abs

data class Participation(
    val id: Long,
    val event: Event,
    val person: Person,
    val startMeters: Int,
    val endMeters: Int?
) {
    val totalMeters = abs(startMeters - (endMeters ?: 0))
}