package com.example.guetteteskilometres.ui.screens.newperson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.repository.PersonRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPersonViewModel(
    val personRepository: PersonRepository
): ViewModel() {
    private val _state = MutableStateFlow(
        NewPersonState(
            name = null,
            firstName = null,
            idErrorMessage = null
        )
    )
    val state: StateFlow<NewPersonState> = _state

    private val _events = MutableSharedFlow<NewPersonEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<NewPersonEvents> = _events

    fun updateName(name: String) {
        _state.update {
            it.copy(name = name)
        }
    }

    fun updateFirstname(firstname: String) {
        _state.update {
            it.copy(firstName = firstname)
        }
    }

    fun validate(idEvent: Long?) {
        idEvent?.let { id ->
            viewModelScope.launch {
                val firstname = _state.value.firstName
                val name = _state.value.name
                if (firstname.isNullOrEmpty()) {
                    _state.update {
                        it.copy(idErrorMessage = R.string.message_error_firstname_missing)
                    }
                } else {
                    personRepository.savePerson(
                        name = name,
                        firstname = firstname,
                        idEvent = id
                    )?.let { idPerson ->
                        _events.emit(NewPersonEvents.NavigateUpWithPerson(idPerson = idPerson))
                    }
                }
            }
        }
    }
}