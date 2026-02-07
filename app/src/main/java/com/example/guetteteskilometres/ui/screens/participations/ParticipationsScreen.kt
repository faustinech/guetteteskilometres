package com.example.guetteteskilometres.ui.screens.participations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.secondaryRed
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ParticipationsScreen(
    navigations: ParticipationsNavigations,
    viewModel: ParticipationsViewModel,
    idEvent: Long
) {
    val state = viewModel.state.collectAsState().value
    viewModel.initialize(idEvent)
    ScreenBody(
        state = state,
        interactions = ParticipationsInteractions(
            onBackClicked = { navigations.navigateUp() },
            onCreationParticipationClicked = {
                viewModel.updateFilter("")
                viewModel.updateDialog(null)
            },
            onParticipationClicked = { idParticipation ->
                viewModel.updateFilter("")
                viewModel.updateDialog(idParticipation)
            },
            onFilterChanged = viewModel::updateFilter,
            onConfirmClotureClicked = viewModel::confirmClotureEvent,
            onDismissDialogClicked = viewModel::dismissDialog,
            onValidateNewParticipantClicked = viewModel::validateNewParticipant,
            onActiveChanged = viewModel::updateActive,
            onNewParticipantFieldChanged = viewModel::updateNewParticipantField,
            onNewParticipantsDismissClicked = viewModel::dismissNewParticipantDialog,
            onPersonChanged = viewModel::updatePerson,
            onAddPersonClicked = viewModel::updateNewPersonDialog,
            onValidateClicked = viewModel::validate,
            onFieldChanged = viewModel::updateField,
            onDeleteParticipation = viewModel::deleteParticipation
        )
    )

    LaunchedEffect(state) {
        viewModel.events.onEach {
            when (it) {
                ParticipationsEvents.NavigateUp -> navigations.navigateUp()
            }
        }.launchIn(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ScreenBody(
    state: ParticipationsState,
    interactions: ParticipationsInteractions
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
                                    .size(18.dp)
                                    .padding(2.dp)
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.title_participations,
                                    state.event?.name.orEmpty()
                                ),
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
        } ,
        floatingActionButton = {
            if (state.participations.isEmpty() || (state.participations.isNotEmpty() && state.participations.first().endMeters != null)) {
                FloatingActionButton(
                    onClick = { interactions.onCreationParticipationClicked() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.participations.isNotEmpty() || (state.participations.isEmpty() && state.filter.isNotEmpty())) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        val totalKilometers =
                            state.participations.filter { it.endMeters != null }.sumOf {
                                abs(
                                    it.startMeters - (it.endMeters ?: 0)
                                )
                            } / 1000f
                        if (state.filter.isEmpty()) {
                            val nbPersons = state.participations.groupBy { it.person }.size
                            Text(
                                text = pluralStringResource(
                                    id = R.plurals.label_nb_participants,
                                    count = nbPersons,
                                    nbPersons
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(
                                id = R.string.label_nb_kilometres_totaux,
                                totalKilometers.toString()
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
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
                        if (state.filter.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable { interactions.onFilterChanged("") }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (state.participations.isNotEmpty()) {
                    // En cours
                    state.participations.firstOrNull { it.endMeters == null }?.let { enCours ->
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                enCours.Compose(
                                    interactions,
                                    modifier = Modifier.padding(bottom = 15.dp)
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    }
                    // Terminé
                    items(
                        items = state.participations.filter { it.endMeters != null }
                    ) { participation ->
                        participation.Compose(interactions)
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(id = R.string.label_no_participation),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        }

        if (state.dialog is Dialog.Input) {
            CreateOrEditParticipationDialog(
                onDismissClicked = interactions.onDismissDialogClicked,
                onAddPersonClicked = interactions.onAddPersonClicked,
                onPersonChanged = interactions.onPersonChanged,
                onFieldChanged = interactions.onFieldChanged,
                onValidateClicked = interactions.onValidateClicked,
                onValidateNewParticipantClicked = interactions.onValidateNewParticipantClicked,
                onActiveChanged = interactions.onActiveChanged,
                onNewParticipantFieldChanged = interactions.onNewParticipantFieldChanged,
                onNewParticipantsDismissClicked = interactions.onNewParticipantsDismissClicked,
                state = state.dialog
            )
        }
    }
}

@Composable
fun Participation.Compose(
    interactions: ParticipationsInteractions,
    modifier: Modifier = Modifier
) {
    val total = if (endMeters != null) {
        abs(startMeters - endMeters)
    } else null
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { interactions.onParticipationClicked(id) },
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
                    text = "${person.firstname} ${person.name?.uppercase() ?: ""}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = if (total != null) {
                        stringResource(id = R.string.text_nb_metres, total)
                    } else stringResource(id = R.string.label_in_progress),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (endMeters == null) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = secondaryRed,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { interactions.onDeleteParticipation(id) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    GuetteTesKilometresTheme {
        val participations = persistentListOf(
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", null, true),
                event = Event(1, "100", 0, true, false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 1,
                person = Person(1, "Luc", "Paul", null, true),
                event = Event(1, "100", 100, true, false, 10000, 12),
                startMeters = 1000,
                endMeters = null
            )
        )
        ScreenBody(
            state = ParticipationsState(
                event = Event(
                    id = 1,
                    name = "100 kilomètres",
                    startMeters = 0,
                    ascending = true,
                    totalMeters = 0,
                    active = false,
                    nbParticipants = null
                ),
                participations = participations,
                filter = "",
                dialog = Dialog.None
            ),
            interactions = ParticipationsInteractions(
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { _, _ -> },
                { },
                { },
                { },
                { },
                { _, _ -> },
                { }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoParticipationPreview() {
    GuetteTesKilometresTheme {
        val participations = persistentListOf<Participation>()
        ScreenBody(
            state = ParticipationsState(
                event = Event(
                    id = 1,
                    name = "100 kilomètres",
                    totalMeters = 0,
                    ascending = true,
                    startMeters = 0,
                    active = false,
                    nbParticipants = null
                ),
                participations = participations,
                filter = "",
                dialog = Dialog.None
            ),
            interactions = ParticipationsInteractions(
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { _, _ -> },
                { },
                { },
                { },
                { },
                { _, _ -> },
                { }
            )
        )
    }
}