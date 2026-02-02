package com.example.guetteteskilometres.ui.screens.participants

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.data.repository.PersonRepository
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

class ParticipantsViewModel @Inject constructor(
    val personRepository: PersonRepository
): BaseViewModel() {
    private var _allPersons = emptyList<Person>()

    private val _state = MutableStateFlow(
        ParticipantsState(
            persons = persistentListOf(),
            activeFilter = ParticipantFilter.All,
            filter = "",
            dialog = null
        )
    )
    val state: StateFlow<ParticipantsState> = _state

    fun initialize() = launchInitState {
        personRepository.getAll().onEach { persons ->
            _allPersons = persons
            _state.update { state ->
                state.copy(persons = persons.toImmutableList())
            }
        }.launchIn(viewModelScope)
    }

    fun updateActive(filter: ParticipantFilter) {
        val filteredPersons = _allPersons.filter {
            when (filter) {
                ParticipantFilter.Active -> it.active
                ParticipantFilter.Inactive -> !it.active
                ParticipantFilter.All -> true
            }
        }
        _state.update { state ->
            state.copy(
                activeFilter = filter,
                persons = filteredPersons.toImmutableList()
            )
        }
    }

    fun updateFilter(filter: String) {
        val filteredPersons = _allPersons.filter {
            val libelle = "${it.firstname} ${it.name.orEmpty()}"
            libelle.lowercase().contains(filter.lowercase())
        }
        _state.update { state ->
            state.copy(
                filter = filter,
                persons = filteredPersons.toImmutableList()
            )
        }
    }

    fun updateField(type: ParticipantField, value: String?) {
        _state.update { state ->
            when (type) {
                ParticipantField.Firstname -> state.copy(
                    dialog = state.dialog?.copy(
                        firstname = value,
                        alert = null
                    )
                )
                ParticipantField.Name -> state.copy(
                    dialog = state.dialog?.copy(
                        name = value,
                        alert = null
                    )
                )
                ParticipantField.Email -> state.copy(
                    dialog = state.dialog?.copy(
                        email = value,
                        alert = null
                    )
                )
            }
        }
    }

    fun updateActive(active: Boolean) {
        _state.update { state ->
            state.copy(
                dialog = state.dialog?.copy(
                    active = active,
                    alert = null
                )
            )
        }
    }

    fun dismissDialog() {
        _state.update { state ->
            state.copy(dialog = null)
        }
    }

    fun updateDialog(idPerson: Long?) {
        viewModelScope.launch {
            val person = idPerson?.let { personRepository.getPerson(it) }
            _state.update { state ->
                state.copy(
                    dialog = state.dialog?.copy(
                        idPerson = person?.id,
                        firstname = person?.firstname,
                        name = person?.name,
                        email = person?.email,
                        active = person?.active,
                        alert = null
                    )
                )
            }
        }
    }

    fun validate() {
        viewModelScope.launch {
            val dialog = _state.value.dialog
            val id = dialog?.idPerson
            val firstname = dialog?.firstname
            val name = dialog?.name
            val email = dialog?.email
            val active = dialog?.active
            if (firstname.isNullOrEmpty()) {
                _state.update { state ->
                    state.copy(
                        dialog = state.dialog?.copy(
                            alert = SaveAlert.MissingFields
                        )
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
                        dialog = state.dialog?.copy(
                            alert = if (idPerson != null) SaveAlert.Success else SaveAlert.Error
                        )
                    )
                }
                idPerson?.let {
                    delay(600)
                    _state.update { state -> state.copy(dialog = null) }
                }
            }
        }
    }
}