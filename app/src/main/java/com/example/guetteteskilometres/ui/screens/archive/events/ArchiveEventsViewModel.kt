package com.example.guetteteskilometres.ui.screens.archive.events

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.enums.ParticipationColumns
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate
import javax.inject.Inject

class ArchiveEventsViewModel @Inject constructor(
    val eventRepository: EventRepository
): BaseViewModel() {
    private var _allEvents = emptyList<Event>()

    private val _state = MutableStateFlow(
        ArchiveEventsState(
            events = persistentListOf(),
            dialog = Dialog.None,
            filter = ""
        )
    )
    val state: StateFlow<ArchiveEventsState> = _state

    fun initialize() = launchInitState {
        eventRepository.getEvents(active = false).onEach { events ->
            val updatedEvents = events.map {
                val nbParticipants = it.participations.groupBy { p -> p.person }.size
                val totalMeters = it.participations.sumOf { p -> p.totalMeters }
                it.copy(
                    nbParticipants = nbParticipants,
                    totalMeters = totalMeters
                )
            }
            _allEvents = updatedEvents
            _state.update { state ->
                state.copy(events = updatedEvents.toImmutableList())
            }
        }.launchIn(viewModelScope)
    }

    fun dismissDialog() {
        _state.update {
            it.copy(
                dialog = Dialog.None
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun exportEvent(id: Long) {
        try {
            val event = _allEvents.find { it.id == id } ?: return
            val dir = Environment.getExternalStorageDirectory().path + File.separator + "Documents"
            val today = LocalDate.now()
            val detailsFile = File(
                dir,
                "${event.name.uppercase().replace(' ', '_')}_" +
                        "${today.year}_" +
                        "${today.monthValue}_" +
                        "${today.dayOfMonth}_DETAILS.csv"
            )
            val recapFile = File(
                dir,
                "${event.name.uppercase().replace(' ', '_')}_" +
                        "${today.year}_" +
                        "${today.monthValue}_" +
                        "${today.dayOfMonth}_RECAP.csv"
            )
            val allParticipations = event.participations
            FileOutputStream(detailsFile).apply { writeDetails(allParticipations) }
            FileOutputStream(recapFile).apply { writeRecap(allParticipations) }
            _state.update {
                it.copy(dialog = Dialog.SuccessSave)
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(dialog = Dialog.ErrorSave)
            }
        }
    }

    private fun OutputStream.writeDetails(participations: List<Participation>) {
        val writer = bufferedWriter()
        val columns = ParticipationColumns.entries.joinToString(separator = ",") { it.display }
        writer.write(columns)
        writer.newLine()
        participations.forEach {
            writer.write("${it.person.firstname}, ${it.person.name.orEmpty()}, ${it.startMeters}, ${it.endMeters}, ${it.totalMeters}")
            writer.newLine()
        }
        writer.flush()
    }

    private fun OutputStream.writeRecap(participations: List<Participation>) {
        data class RecapCsvItem(
            val firstname: String,
            val name: String,
            val totalMeters: Int
        )

        val writer = bufferedWriter()
        val columnsRecap = ParticipationColumns.entries.filter { it.inRecap }.joinToString(separator = ",") { it.display }
        writer.write(columnsRecap)
        writer.newLine()
        participations.groupBy { it.person }
            .map { (person, participations) ->
                RecapCsvItem(
                    firstname = person.firstname,
                    name = person.name.orEmpty(),
                    totalMeters = participations.sumOf { it.totalMeters }
                )
            }.sortedByDescending { it.totalMeters }
            .forEach {
                writer.write("${it.firstname}, ${it.name}, ${it.totalMeters}")
                writer.newLine()
            }
        writer.flush()
    }

    fun updateFilter(filter: String) {
        val filteredEvents = _allEvents.filter {
            it.name.lowercase().contains(filter.lowercase())
        }
        _state.update { state ->
            state.copy(
                filter = filter,
                events = filteredEvents.toImmutableList()
            )
        }
    }
}