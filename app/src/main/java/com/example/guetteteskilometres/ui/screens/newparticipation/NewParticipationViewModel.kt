package com.example.guetteteskilometres.ui.screens.newparticipation

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewParticipationViewModel(
    val participationRepository: ParticipationRepository,
    val personRepository: PersonRepository,
    val eventRepository: EventRepository
): BaseViewModel() {

    private val _state = MutableStateFlow(
        NewParticipationState(
            person = null,
            event = null,
            persons = persistentListOf(),
            startMeters = null,
            endMeters = null,
            idStartErrorMessage = null,
            idPersonErrorMessage = null,
            idEndErrorMessage = null
        )
    )
    val state: StateFlow<NewParticipationState> = _state

    private val _events = MutableSharedFlow<NewParticipationEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<NewParticipationEvents> = _events

    fun initialize(idEvent: Long?, idParticipation: Long?) = launchInitStateAsync {
        idEvent?.let {
            val participation = idParticipation?.let { id -> participationRepository.getParticipation(id) }
            _state.update {
                it.copy(
                    person = it.person ?: participation?.person,
                    event = participation?.event ?: eventRepository.getEvent(idEvent),
                    persons = personRepository.getPersons(idEvent).toImmutableList(),
                    startMeters = participation?.startMeters,
                    endMeters = participation?.endMeters
                )
            }
        }
    }

    fun updateStartMeters(meters: String) {
        try {
            _state.update {
                it.copy(startMeters = meters.toInt())
            }
        } catch (e: NumberFormatException) {
            _state.update {
                it.copy(idStartErrorMessage = R.string.message_valeur_incorrect)
            }
        }
    }

    fun updateEndMeters(meters: String) {
        try {
            _state.update {
                it.copy(endMeters = meters.toInt())
            }
        } catch (e: NumberFormatException) {
            _state.update {
                it.copy(idEndErrorMessage = R.string.message_valeur_incorrect)
            }
        }
    }

    fun updatePerson(person: Person) {
        _state.update {
            it.copy(person = person)
        }
    }

    fun updatePerson(idPerson: Long) {
        viewModelScope.launch {
            personRepository.getPerson(idPerson)?.let { person ->
                _state.update {
                    it.copy(person = person)
                }
            }
        }
    }

    fun validate() {
        viewModelScope.launch {
            val startMeters = _state.value.startMeters
            val endMeters = _state.value.endMeters
            val person = _state.value.person
            val event = _state.value.event
            if (startMeters == null || person == null) {
                if (startMeters == null) {
                    _state.update {
                        it.copy(idStartErrorMessage = R.string.message_error_missing_start_meters)
                    }
                } else {
                    _state.update {
                        it.copy(idPersonErrorMessage = R.string.message_error_missing_person)
                    }
                }
            } else {
                participationRepository.saveParticipation(
                    startMeters = startMeters,
                    endMeters = endMeters,
                    person = person,
                    event = event ?: return@launch
                )
                _events.emit(NewParticipationEvents.NavigateUp)
            }
        }
    }
}