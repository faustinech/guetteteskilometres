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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewParticipationViewModel(
    val participationRepository: ParticipationRepository,
    val personRepository: PersonRepository,
    val eventRepository: EventRepository
): BaseViewModel() {
    private var _idParticipation: Long? = null

    private val _state = MutableStateFlow(
        NewParticipationState(
            person = null,
            event = null,
            persons = persistentListOf(),
            startMeters = null,
            endMeters = null,
            idStartErrorMessage = null,
            idPersonErrorMessage = null,
            idEndErrorMessage = null,
            dialog = Dialog.None,
            libellePerson = "",
            isLastInput = false
        )
    )
    val state: StateFlow<NewParticipationState> = _state

    private val _events = MutableSharedFlow<NewParticipationEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<NewParticipationEvents> = _events

    fun initialize(idEvent: Long?, idParticipation: Long?, isLastParticipation: Boolean) = launchInitStateAsync {
        _idParticipation = idParticipation
        idEvent?.let {
            val participation = idParticipation?.let { id -> participationRepository.getParticipation(id) }
            val startMeters = participation?.startMeters ?: participationRepository.getLastParticipation(idEvent)?.endMeters
            personRepository.getPersons(idEvent).onEach { persons ->
                _state.update {
                    val person = it.person ?: participation?.person
                    it.copy(
                        person = person,
                        event = participation?.event ?: eventRepository.getEvent(idEvent),
                        persons = persons.toImmutableList(),
                        startMeters = startMeters,
                        endMeters = participation?.endMeters,
                        libellePerson = if (person?.name != null) {
                            "${person.firstname} ${person.name}"
                        } else if (person?.firstname != null) {
                            person.firstname
                        } else "",
                        isLastInput = isLastParticipation
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateStartMeters(meters: String) {
        try {
            _state.update {
                it.copy(startMeters = if (meters.isEmpty()) 0 else meters.toInt())
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
                it.copy(endMeters = if (meters.isEmpty()) null else meters.toInt())
            }
        } catch (e: NumberFormatException) {
            _state.update {
                it.copy(idEndErrorMessage = R.string.message_valeur_incorrect)
            }
        }
    }

    fun updatePerson(person: Person) {
        _state.update {
            it.copy(
                person = person,
                libellePerson = if (person.name == null) {
                    person.firstname
                } else {
                    "${person.firstname} ${person.name}"
                }
            )
        }
    }

    fun updatePerson(idPerson: Long) {
        viewModelScope.launch {
            personRepository.getPerson(idPerson)?.let { person ->
                _state.update {
                    it.copy(
                        person = person,
                        libellePerson = if (person.name == null) {
                            person.firstname
                        } else {
                            "${person.firstname} ${person.name}"
                        }
                    )
                }
            }
        }
    }

    fun dialogDismiss() {
        _state.update {
            it.copy(dialog = Dialog.None)
        }
    }

    fun requestDelete() {
        _state.update {
            it.copy(dialog = Dialog.ConfirmSuppressionParticipation)
        }
    }

    fun delete() {
        viewModelScope.launch {
            _idParticipation?.let { id ->
                participationRepository.removeParticipation(id)
            }
            _events.emit(NewParticipationEvents.NavigateUp)
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
                    idParticipation = if (_idParticipation != -1L) _idParticipation else 0,
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