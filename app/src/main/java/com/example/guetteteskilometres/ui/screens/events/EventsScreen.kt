package com.example.guetteteskilometres.ui.screens.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.secondaryRed
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

@Composable
fun EventsScreen(
    navigations: EventsNavigations,
    viewModel: EventsViewModel
) {
    val state by viewModel.state.collectAsState()

    viewModel.initialize()

    ScreenBody(
        state = state,
        interactions = EventsInteractions(
            onEventClicked = { event -> navigations.navigateToEvent(event) },
            onBackClicked = navigations.navigateUp,
            onNewEventClicked = { viewModel.updateDialog(idEvent = null) },
            onFilterChanged = { filter -> viewModel.updateFilter(filter) },
            onDeleteEventClicked = { event -> viewModel.deleteEvent(event) },
            onConfirmClicked = { viewModel.confirmDeleteEvent() },
            onDismissClicked = { viewModel.dismissDialog() },
            onEditEventClicked = { idEvent -> viewModel.updateDialog(idEvent) },
            onValidateClicked = { viewModel.validate() },
            onAscendingChanged = { ascending -> viewModel.updateAscending(ascending) },
            onActiveChanged = { active -> viewModel.updateActive(active) },
            onFieldChanged = { type, value -> viewModel.updateField(type, value) }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ScreenBody(
    state: EventsState,
    interactions: EventsInteractions
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { interactions.onBackClicked() }
                                    .padding(2.dp)
                                    .size(18.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.title_events),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { interactions.onNewEventClicked() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        when (val dialog = state.dialog) {
            is Dialog.ConfirmSuppression -> AlertDialog(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_confirmation),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            id = R.string.message_confirmation_suppression_event,
                            dialog.libelle
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onDismissRequest = interactions.onDismissClicked,
                confirmButton = {
                    OutlinedButton(
                        onClick = interactions.onConfirmClicked,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.common_yes))
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = interactions.onDismissClicked,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.common_no))
                    }
                },
                containerColor = MaterialTheme.colorScheme.background
            )
            is Dialog.Input -> {
                CreateOrEditEventDialog(
                    state = dialog,
                    onValidateClicked = interactions.onValidateClicked,
                    onFieldChanged = interactions.onFieldChanged,
                    onActiveChanged = interactions.onActiveChanged,
                    onDismissClicked = interactions.onDismissClicked,
                    onAscendingChanged = interactions.onAscendingChanged
                )
            }
            Dialog.None -> { }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = state.filter,
                        onValueChange = interactions.onFilterChanged,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.label_filter),
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable { interactions.onFilterChanged("") }
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            if (state.events.isNotEmpty()) {
                items(state.events) { event ->
                    event.Compose(
                        onEventClicked = interactions.onEventClicked,
                        onDeleteEventClicked = interactions.onDeleteEventClicked,
                        onEditEventClicked = interactions.onEditEventClicked
                    )
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.label_no_event),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Event.Compose(
    onEventClicked: (Long) -> Unit,
    onDeleteEventClicked: (Event) -> Unit,
    onEditEventClicked: (Long) -> Unit
) {
    val event = this
    val nbParticipants = nbParticipants
    val kilometers = totalMeters?.div(1000f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClicked(event.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.text_nb_participants_nb_kilometres,
                        nbParticipants ?: 0,
                        nbParticipants ?: 0,
                        kilometers?.roundToInt() ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(24.dp)
                    .clickable { onEditEventClicked(event.id) }
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = secondaryRed,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteEventClicked(event) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsPreview() {
    GuetteTesKilometresTheme {
        val events = persistentListOf(
            Event(
                id = 0,
                name = "100 kilomètres",
                startMeters = 100,
                ascending = false,
                active = true,
                totalMeters = 100000,
                nbParticipants = 28
            ),
            Event(
                id = 1,
                name = "24 heures",
                startMeters = 0,
                ascending = true,
                active = false,
                totalMeters = 0,
                nbParticipants = 0
            )
        )
        ScreenBody(
            state = EventsState(
                events = events,
                dialog = Dialog.None,
                idEventToDelete = null,
                filter = ""
            ),
            interactions = EventsInteractions(
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { _, _ -> }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoEventPreview() {
    GuetteTesKilometresTheme {
        val events = persistentListOf<Event>()
        ScreenBody(
            state = EventsState(
                events = events,
                dialog = Dialog.ConfirmSuppression("24 heures"),
                idEventToDelete = null,
                filter = ""
            ),
            interactions = EventsInteractions(
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { _, _ -> }
            )
        )
    }
}