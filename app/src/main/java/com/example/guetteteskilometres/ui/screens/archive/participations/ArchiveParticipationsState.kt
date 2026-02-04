package com.example.guetteteskilometres.ui.screens.archive.participations

import com.example.guetteteskilometres.data.model.ArchiveParticipation
import com.example.guetteteskilometres.data.model.Event
import kotlinx.collections.immutable.ImmutableList

data class ArchiveParticipationsState(
    val event: Event?,
    val participations: ImmutableList<ArchiveParticipation>,
    val filter: String
)