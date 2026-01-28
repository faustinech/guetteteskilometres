package com.example.guetteteskilometres.ui.screens.events

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {

    private var _allEvents = emptyList<Event>()

    private val _state = MutableStateFlow(
        EventsState(
            events = persistentListOf(),
            dialog = Dialog.None,
            idEventToDelete = null,
            filter = ""
        )
    )
    val state: StateFlow<EventsState> = _state

    fun initialize() = launchInitState {
        eventRepository.getEvents().onEach { events ->
            val updatedEvents = events.map {
                val nbParticipants = it.participations.groupBy { p -> p.person }.size
                val totalMeters = it.participations.sumOf { p -> p.totalMeters }
                it.copy(nbParticipants = nbParticipants, totalMeters = totalMeters)
            }
            _allEvents = updatedEvents
            _state.update { state ->
                state.copy(events = updatedEvents.toImmutableList())
            }
        }.launchIn(viewModelScope)
    }

    fun deleteEvent(event: Event) {
        _state.update {
            it.copy(
                dialog = Dialog.ConfirmSuppression(libelle = event.name),
                idEventToDelete = event.id
            )
        }
    }

    fun confirmDeleteEvent() {
        viewModelScope.launch {
            val idEvent = _state.value.idEventToDelete ?: return@launch
            eventRepository.deleteEvent(idEvent)
            _state.update {
                it.copy(
                    idEventToDelete = null,
                    dialog = Dialog.None
                )
            }
        }
    }

    fun dismissDialog() {
        _state.update {
            it.copy(
                dialog = Dialog.None,
                isEditDialogVisible = false,
                idEventToDelete = null
            )
        }
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

    fun updateField(type: EventField, value: String?) {
        _state.update { state ->
            when (type) {
                EventField.Name -> state.copy(name = value, alert = null)
                EventField.StartMeters -> state.copy(startMeters = value, alert = null)
            }
        }
    }

    fun updateActive(active: Boolean) {
        _state.update { state ->
            state.copy(active = active, alert = null)
        }
    }

    fun updateAscending(ascending: Boolean) {
        _state.update { state ->
            state.copy(active = ascending, alert = null)
        }
    }

    fun updateDialog(idEvent: Long?) {
        viewModelScope.launch {
            val event = idEvent?.let { eventRepository.getEvent(it) }
            _state.update { state ->
                state.copy(
                    idEvent = event?.id,
                    name = event?.name,
                    startMeters = event?.startMeters?.toString(),
                    active = event?.active,
                    ascending = event?.ascending,
                    isEditDialogVisible = true,
                    alert = null
                )
            }
        }
    }

    fun validate() {
        viewModelScope.launch {
            val id = _state.value.idEvent
            val name = _state.value.name
            val startMeters = _state.value.startMeters
            val active = _state.value.active
            val ascending = _state.value.ascending
            if (name.isNullOrEmpty() || startMeters.isNullOrEmpty()) {
                _state.update { state ->
                    state.copy(alert = SaveAlert.MissingFields)
                }
            } else {
                val idEvent = eventRepository.saveEvent(
                    id = id,
                    name = name,
                    startMeters = startMeters,
                    ascending = ascending,
                    active = active
                )
                _state.update { state ->
                    state.copy(alert = if (idEvent != null) SaveAlert.Success else SaveAlert.Error)
                }
                idEvent?.let {
                    delay(1000)
                    _state.update { state -> state.copy(isEditDialogVisible = false) }
                }
            }
        }
    }
}