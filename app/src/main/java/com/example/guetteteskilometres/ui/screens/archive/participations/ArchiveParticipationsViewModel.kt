package com.example.guetteteskilometres.ui.screens.archive.participations

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.ArchiveParticipation
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.math.abs

class ArchiveParticipationsViewModel(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {
    private var _allParticipations = emptyList<ArchiveParticipation>()

    private val _state= MutableStateFlow(
        ArchiveParticipationsState(
            event = null,
            participations = persistentListOf(),
            filter = ""
        )
    )
    val state: StateFlow<ArchiveParticipationsState> = _state

    fun initialize(idEvent: Long) = launchInitState {
        participationRepository.getParticipations(idEvent).onEach { participations ->
            val event = eventRepository.getEvent(idEvent)
            val archiveParticipations = participations.groupBy { it.person }.map {
                ArchiveParticipation(
                    person = it.key,
                    totalMeters = it.value.sumOf { p ->
                        abs(p.startMeters - (p.endMeters ?: 0))
                    }
                )
            }.sortedBy { it.person.name }
            _allParticipations = archiveParticipations
            _state.update { state ->
                state.copy(
                    event = event,
                    participations = archiveParticipations.toImmutableList()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateFilter(filter: String) {
        val filteredParticipations = _allParticipations.filter {
            val libelle = "${it.person.firstname} ${it.person.name.orEmpty()}"
            libelle.lowercase().contains(filter.lowercase())
        }
        _state.update {
            it.copy(
                participations = filteredParticipations.toImmutableList(),
                filter = filter
            )
        }
    }
}