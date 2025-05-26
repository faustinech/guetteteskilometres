package com.example.guetteteskilometres.ui.screens.participations

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.data.repository.ParticipationRepository
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
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate


class ParticipationsViewModel(
    val eventRepository: EventRepository,
    val participationRepository: ParticipationRepository
): BaseViewModel() {

    private var allParticipations = emptyList<Participation>()

    private val _state = MutableStateFlow(
        ParticipationsState(
            event = null,
            participations = persistentListOf(),
            filter = null,
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

    fun updateFilter(filter: String?) {
        val filteredParticipations = allParticipations.filter {
            val libelle = "${it.person.firstname} ${it.person.name.orEmpty()}"
            libelle.lowercase().contains(filter.orEmpty().lowercase())
        }
        _state.update {
            it.copy(
                participations = filteredParticipations.toImmutableList(),
                filter = filter
            )
        }
    }

    fun closeEvent() {
        _state.update {
            it.copy(dialog = Dialog.ConfirmCloture)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveData() {
        try {
            val event = _state.value.event ?: return
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
            FileOutputStream(detailsFile).apply { writeDetails(allParticipations) }
            FileOutputStream(recapFile).apply { writeRecap(allParticipations) }
            _state.update {
                it.copy(dialog = Dialog.SucessSave)
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
}