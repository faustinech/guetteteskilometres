package com.example.guetteteskilometres.ui.screens.home

import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {
    private val _state = MutableStateFlow(
        HomeState(events = persistentListOf())
    )
    val state: StateFlow<HomeState> = _state

    fun initialize() = launchInitStateAsync {
        eventRepository.getEvents().let { events ->
            events.forEach {
                val participations = participationRepository.getParticipations(it.id)
                it.nbParticipants = participations.groupBy { p -> p.person }.size
                it.totalMeters = participations.sumOf { p -> p.totalMeters }
            }
            _state.update {
                it.copy(
                    events = events.toImmutableList()
                )
            }
        }
    }
}