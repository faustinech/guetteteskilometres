package com.example.guetteteskilometres.ui.screens.newevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.repository.EventRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewEventViewModel(
    val eventRepository: EventRepository
): ViewModel() {

    private val _state = MutableStateFlow(
        NewEventState(
            name = null,
            idErrorMessage = null
        )
    )
    val state: StateFlow<NewEventState> = _state

    private val _events = MutableSharedFlow<NewEventEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<NewEventEvents> = _events

    fun updateName(name: String) {
        _state.update {
            it.copy(name = name, idErrorMessage = null)
        }
    }

    fun validate() {
        viewModelScope.launch {
            val name = _state.value.name
            if (name.isNullOrEmpty() || name.isBlank()) {
                _state.update {
                    it.copy(idErrorMessage = R.string.message_error_name_event)
                }
            } else {
                eventRepository.saveEvent(name)
                _events.tryEmit(NewEventEvents.NavigateUp)
            }
        }
    }
}