package com.example.guetteteskilometres.ui.screens.archive

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ArchiveEventsViewModel @Inject constructor(
    val eventRepository: EventRepository
): BaseViewModel() {
    private var _allEvents = emptyList<Event>()

    private val _state = MutableStateFlow(
        ArchiveEventsState(
            events = persistentListOf(),
            dialog = Dialog.None,
            filter = ""
        )
    )
    val state: StateFlow<ArchiveEventsState> = _state

    fun initialize() = launchInitState {
        eventRepository.getEvents(active = false).onEach { events ->
            val updatedEvents = events.map {
                val nbParticipants = it.participations.groupBy { p -> p.person }.size
                val totalMeters = it.participations.sumOf { p -> p.totalMeters }
                it.copy(
                    nbParticipants = nbParticipants,
                    totalMeters = totalMeters
                )
            }
            _allEvents = updatedEvents
            _state.update { state ->
                state.copy(events = updatedEvents.toImmutableList())
            }
        }.launchIn(viewModelScope)
    }

    fun dismissDialog() {
        _state.update {
            it.copy(
                dialog = Dialog.None
            )
        }
    }

    fun exportEvent(id: Long) {
        // TODO FCH : ajouter l'export comme c'était fait avant
    }

    fun updateFilter(filter: String) {
        val filteredEvents = _allEvents.filter {
            it.name.lowercase().contains(filter.lowercase())
        }
        _state.update { state ->
            state.copy(
                filter = filter,
                events = filteredEvents.toImmutableList()
            )
        }
    }
}