package com.example.guetteteskilometres.ui.screens.participants

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantField
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import com.example.guetteteskilometres.data.repository.PersonRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ParticipantsViewModel @Inject constructor(
    val personRepository: PersonRepository
): BaseViewModel() {
    private var _allPersons = emptyList<Person>()

    private val _state = MutableStateFlow(
        ParticipantsState(
            persons = persistentListOf(),
            activeFilter = ParticipantFilter.All,
            filter = ""
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
                ParticipantField.Firstname -> state.copy(firstname = value)
                ParticipantField.Name -> state.copy(name = value)
                ParticipantField.Email -> state.copy(email = value)
            }
        }
    }

    fun updateActive(active: Boolean) {
        _state.update { state ->
            state.copy(active = active)
        }
    }

    fun dismissDialog() {
        _state.update { state ->
            state.copy(isDialogVisible = false)
        }
    }

    fun updateDialog(idPerson: Long?) {

    }

    fun validate() {

    }
}