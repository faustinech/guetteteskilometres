package com.example.guetteteskilometres.ui.screens.participations

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParticipationsViewModel(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {

    private var allParticipations = emptyList<Participation>()

    private val _state = MutableStateFlow(
        ParticipationsState(
            event = null,
            participations = persistentListOf(),
            filter = null,
            dialog = Dialog.None
        )
    )
    val state: StateFlow<ParticipationsState> = _state

    private val _events = MutableSharedFlow<ParticipationsEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<ParticipationsEvents> = _events

    fun initialize(idEvent: Long?) = launchInitState {
        idEvent?.let {
            participationRepository.getParticipations(idEvent).onEach { participations ->
                allParticipations = participations
                _state.update {
                    it.copy(
                        event = eventRepository.getEvent(idEvent),
                        participations = allParticipations.toImmutableList()
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateFilter(filter: String?) {
        // TODO : filter sur autre chose que le prénom ?
        val filteredParticipations = allParticipations.filter { it.person.firstname.lowercase().contains(filter.orEmpty().lowercase()) }
        _state.update {
            it.copy(
                participations = filteredParticipations.toImmutableList(),
                filter = filter
            )
        }
    }

    fun closeEvent() {
        _state.update {
            it.copy(dialog = Dialog.ConfirmCloture)
        }
    }

    fun confirmClotureEvent() {
        viewModelScope.launch {
            val event = _state.value.event ?: return@launch
            eventRepository.closeEvent(event.id)
            _events.emit(ParticipationsEvents.NavigateUp)
        }
    }

    fun dismissDialog() {
        _state.update {
            it.copy(dialog = Dialog.None)
        }
    }
}