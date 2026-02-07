package com.example.guetteteskilometres.ui.screens.participations

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import com.example.guetteteskilometres.ui.screens.participants.CreateOrEditParticipantDialogInfos
import com.example.guetteteskilometres.ui.screens.participants.ParticipantField
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ParticipationsViewModel(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository,
    val personRepository: PersonRepository
): BaseViewModel() {

    private var allParticipations = emptyList<Participation>()

    private val _state = MutableStateFlow(
        ParticipationsState(
            event = null,
            participations = persistentListOf(),
            filter = "",
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

    fun updateFilter(filter: String) {
        val filteredParticipations = allParticipations.filter {
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

    fun updateNewParticipantField(type: ParticipantField, value: String?) {
        _state.update { state ->
            when (type) {
                ParticipantField.Firstname -> state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                                firstname = value,
                                alert = null
                            )
                        )
                    } else state.dialog
                )
                ParticipantField.Name -> state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                                name = value,
                                alert = null
                            )
                        )
                    } else state.dialog
                )
                ParticipantField.Email -> state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                                email = value,
                                alert = null
                            )
                        )
                    } else state.dialog
                )
            }
        }
    }

    fun updateActive(active: Boolean) {
        _state.update { state ->
            state.copy(
                dialog = if (state.dialog is Dialog.Input) {
                    state.dialog.copy(
                        createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                            active = active,
                            alert = null
                        )
                    )
                } else state.dialog
            )
        }
    }

    fun dismissNewParticipantDialog() {
        _state.update { state ->
            state.copy(
                dialog = if (state.dialog is Dialog.Input) {
                    state.dialog.copy(
                        createOrEditParticipantDialogInfos = null
                    )
                } else state.dialog
            )
        }
    }

    fun updateNewPersonDialog() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            createOrEditParticipantDialogInfos = CreateOrEditParticipantDialogInfos()
                        )
                    } else state.dialog
                )
            }
        }
    }

    fun updateDialog(idParticipation: Long?) {
        viewModelScope.launch {
            val participation = if (idParticipation != null) participationRepository.getParticipation(idParticipation) else null
            _state.update { state ->
                state.copy(
                    dialog = Dialog.Input(
                        idParticipation = participation?.id,
                        person = participation?.person,
                        startMeters = participation?.startMeters?.toString()
                            ?: allParticipations.firstOrNull()?.endMeters?.toString()
                            ?: _state.value.event?.startMeters.toString(),
                        endMeters = participation?.endMeters?.toString(),
                        persons = personRepository.getAll().first().filter { it.active }.toImmutableList(),
                        alert = null,
                        createOrEditParticipantDialogInfos = null
                    )
                )
            }
        }
    }

    fun validateNewParticipant() {
        viewModelScope.launch {
            _state.value.dialog.let { dialog ->
                if (dialog is Dialog.Input) {
                    val dialogNewPerson = dialog.createOrEditParticipantDialogInfos
                    val id = dialogNewPerson?.idPerson
                    val firstname = dialogNewPerson?.firstname
                    val name = dialogNewPerson?.name
                    val email = dialogNewPerson?.email
                    val active = dialogNewPerson?.active
                    if (firstname.isNullOrEmpty()) {
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                                            alert = SaveAlert.MissingFields
                                        )
                                    )
                                } else state.dialog
                            )
                        }
                    } else {
                        val idPerson = personRepository.savePerson(
                            id = id,
                            name = name,
                            firstname = firstname,
                            email = email,
                            active = active
                        )
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        createOrEditParticipantDialogInfos = state.dialog.createOrEditParticipantDialogInfos?.copy(
                                            alert = if (idPerson != null) SaveAlert.Success else SaveAlert.Error
                                        )
                                    )
                                } else state.dialog
                            )
                        }
                        idPerson?.let {
                            delay(600)
                            _state.update { state ->
                                state.copy(
                                    dialog = if (state.dialog is Dialog.Input) {
                                        state.dialog.copy(
                                            person = personRepository.getPerson(idPerson),
                                            createOrEditParticipantDialogInfos = null
                                        )
                                    } else state.dialog,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun updatePerson(person: Person) {
        _state.update { state ->
            state.copy(
                dialog = if (state.dialog is Dialog.Input) {
                    state.dialog.copy(
                        person = person
                    )
                } else state.dialog
            )
        }
    }

    fun validate() {
        viewModelScope.launch {
            val saisie = _state.value.dialog
            if (saisie is Dialog.Input) {
                try {
                    val startMeters = saisie.startMeters?.toIntOrNull()
                    val endMeters = saisie.endMeters?.toIntOrNull()
                    val person = saisie.person
                    val event = _state.value.event
                    val lastParticipation = allParticipations.firstOrNull { it.endMeters != null }
                    val beforeParticipation = allParticipations.firstOrNull { it.id != lastParticipation?.id }
                    val isEdition = lastParticipation?.id == saisie.idParticipation
                    val defaultStartMeters = when {
                        isEdition -> beforeParticipation?.endMeters
                        else -> lastParticipation?.endMeters
                    } ?: _state.value.event?.startMeters
                    if (startMeters == null || person == null) {
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        alert = SaveAlert.MissingFields
                                    )
                                } else state.dialog
                            )
                        }
                    } else if (startMeters < 0 || (endMeters != null && endMeters < 0)) {
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        alert = SaveAlert.Incoherence
                                    )
                                } else state.dialog
                            )
                        }
                    } else if (
                        (event?.ascending == true && (startMeters > (endMeters ?: 0))) ||
                        (event?.ascending == false && (startMeters < (endMeters ?: 0))) ||
                        (startMeters != defaultStartMeters)
                    ) {
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        alert = SaveAlert.Incoherence
                                    )
                                } else state.dialog
                            )
                        }
                    } else {
                        val idParticipation = participationRepository.saveParticipation(
                            idParticipation = saisie.idParticipation ?: 0,
                            startMeters = startMeters,
                            endMeters = endMeters,
                            person = person,
                            event = event ?: return@launch
                        )
                        _state.update { state ->
                            state.copy(
                                dialog = if (state.dialog is Dialog.Input) {
                                    state.dialog.copy(
                                        alert = if (idParticipation != null) SaveAlert.Success else SaveAlert.Error
                                    )
                                } else state.dialog
                            )
                        }
                        idParticipation?.let {
                            delay(600)
                            _state.update { state ->
                                state.copy(
                                    dialog = Dialog.None,
                                )
                            }
                        }
                    }
                } catch (_: NumberFormatException) {
                    _state.update { state ->
                        state.copy(
                            dialog = if (state.dialog is Dialog.Input) {
                                state.dialog.copy(
                                    alert = SaveAlert.Error
                                )
                            } else state.dialog
                        )
                    }
                }
            }
        }
    }

    fun deleteParticipation(id: Long) {
        viewModelScope.launch {
            participationRepository.removeParticipation(id)
        }
    }

    fun updateField(type: ParticipationField, value: String?) {
        when (type) {
            ParticipationField.StartMeters -> _state.update { state ->
                state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            startMeters = value,
                            alert = null
                        )
                    } else state.dialog
                )
            }
            ParticipationField.EndMeters -> _state.update { state ->
                state.copy(
                    dialog = if (state.dialog is Dialog.Input) {
                        state.dialog.copy(
                            endMeters = value,
                            alert = null
                        )
                    } else state.dialog
                )
            }
        }
    }
}