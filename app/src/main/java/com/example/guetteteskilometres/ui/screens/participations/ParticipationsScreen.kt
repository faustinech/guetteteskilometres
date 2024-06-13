package com.example.guetteteskilometres.ui.screens.participations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.data.model.Participation
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.ui.components.CustomCard
import com.example.guetteteskilometres.ui.components.CustomField
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.dark
import com.example.guetteteskilometres.ui.theme.done
import com.example.guetteteskilometres.ui.theme.light
import com.example.guetteteskilometres.ui.theme.valid
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs

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
            onCreationParticipationClicked = { navigations.navigateToParticipation(idEvent, null) },
            onParticipationClicked = { idParticipation -> navigations.navigateToParticipation(idEvent, idParticipation) },
            onClotureEventClicked = viewModel::closeEvent,
            onFilterChanged = viewModel::updateFilter,
            onConfirmClotureClicked = viewModel::confirmClotureEvent,
            onDismissClotureClicked = viewModel::dismissDialog
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: ParticipationsState,
    interactions: ParticipationsInteractions
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_participations)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light
                ),
                modifier = Modifier.padding(bottom = 5.dp),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null,
                        tint = light,
                        modifier = Modifier
                            .clickable { interactions.onBackClicked() }
                            .padding(horizontal = 5.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            if (state.event?.isDone != true) {
                Row(
                    modifier = Modifier
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    FloatingActionButton(
                        onClick = interactions.onCreationParticipationClicked,
                        containerColor = valid
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    }
                    FloatingActionButton(
                        onClick = interactions.onClotureEventClicked,
                        containerColor = done,
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (state.dialog) {
            Dialog.ConfirmCloture -> AlertDialog(
                title = {
                    Text(text = stringResource(id = R.string.title_confirmation))
                },
                text = {
                    Text(text = stringResource(id = R.string.message_confirmation_cloture))
                },
                onDismissRequest = interactions.onDismissClotureClicked,
                confirmButton = {
                    Button(onClick = interactions.onConfirmClotureClicked) {
                        Text(text = stringResource(id = R.string.common_yes))
                    }
                },
                dismissButton = {
                    Button(onClick = interactions.onDismissClotureClicked) {
                        Text(text = stringResource(id = R.string.common_no))
                    }
                }
            )
            Dialog.None -> { }
        }

        if (state.participations.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    val totalKilometers =
                        state.participations.sumOf {
                            abs(
                                it.startMeters - (it.endMeters ?: 0)
                            )
                        } / 1000f
                    Text(text = state.event?.name.orEmpty())
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(
                            id = R.string.label_nb_kilometres_totaux,
                            totalKilometers.toString()
                        )
                    )
                }
                CustomField(
                    idLabel = R.string.label_filter,
                    value = state.filter,
                    idErrorMessage = null,
                    onValueChange = interactions.onFilterChanged
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(state.participations) {
                        it.Compose()
                    }
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.label_no_participation, state.event?.name.orEmpty()),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun Participation.Compose() {
    val total = abs(startMeters - (endMeters ?: 0))
    CustomCard(
        title = "${person.firstname} ${person.name ?: ""}",
        leftText = stringResource(id = R.string.text_nb_metres, total),
        rightText = null,
        backgroundColor = light
    ) {
        // TODO
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    GuetteTesKilometresTheme {
        val participations = persistentListOf(
            Participation(
                id = 0,
                person = Person(1, "Test", "Nom", 1),
                event = Event(1, "100", false, 1234, 12),
                startMeters = 0,
                endMeters = 1000
            ),
            Participation(
                id = 1,
                person = Person(1, "Luc", "Paul", 1),
                event = Event(1, "100", false, 10000, 12),
                startMeters = 1000,
                endMeters = 3000
            )
        )
        ScreenBody(
            state = ParticipationsState(
                event = Event(
                    id = 1,
                    name = "100 kilomètres",
                    totalMeters = 0,
                    isDone = false,
                    nbParticipants = null
                ),
                participations = participations,
                filter = null,
                dialog = Dialog.None
            ),
            interactions = ParticipationsInteractions(
                { }, { }, { }, { }, { }, { }, { }
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
                    isDone = false,
                    nbParticipants = null
                ),
                participations = participations,
                filter = "",
                dialog = Dialog.ConfirmCloture
            ),
            interactions = ParticipationsInteractions(
                { }, { }, { }, { }, { }, { }, { }
            )
        )
    }
}