package com.example.guetteteskilometres.ui.screens.home

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guetteteskilometres.data.model.Event
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {
    private val _state = MutableStateFlow(
        HomeState(
            events = persistentListOf(),
            dialog = Dialog.None,
            idEventToDelete = null
        )
    )
    val state: StateFlow<HomeState> = _state

    fun initialize() = launchInitState {
        eventRepository.getEvents().onEach { events ->
            val updatedEvents = events.map {
                val nbParticipants = it.participations.groupBy { p -> p.person }.size
                val totalMeters = it.participations.sumOf { p -> p.totalMeters }
                it.copy(nbParticipants = nbParticipants, totalMeters = totalMeters)
            }
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
                idEventToDelete = null
            )
        }
    }
}