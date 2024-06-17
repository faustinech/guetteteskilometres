package com.example.guetteteskilometres.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {
    private val _state = MutableStateFlow(
        HomeState(events = persistentListOf())
    )
    val state: StateFlow<HomeState> = _state

    fun initialize() = launchInitState {
        eventRepository.getEvents().onEach { events ->
            events.forEach {
                participationRepository.getParticipations(it.id).onEach { participations ->
                    it.nbParticipants = participations.groupBy { p -> p.person }.size
                    it.totalMeters = participations.sumOf { p -> p.totalMeters }
                }.launchIn(viewModelScope)
            }
            _state.update {
                it.copy(
                    events = events.toImmutableList()
                )
            }
        }.launchIn(viewModelScope)
    }
}