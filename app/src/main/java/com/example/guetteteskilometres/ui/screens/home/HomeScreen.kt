package com.example.guetteteskilometres.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Event
import com.example.guetteteskilometres.ui.components.CustomCard
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.background
import com.example.guetteteskilometres.ui.theme.dark
import com.example.guetteteskilometres.ui.theme.done
import com.example.guetteteskilometres.ui.theme.light
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeScreen(
    navigations: HomeNavigations,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()

    viewModel.initialize()

    ScreenBody(
        state = state,
        interactions = HomeInteractions(
            onEventClicked = { event -> navigations.navigateToEvent(event) },
            onAddEventClicked = navigations.navigateToNewEvent,
            onConfirmDialog = viewModel::confirmDeleteEvent,
            onDismissDialog = viewModel::dismissDialog,
            onEventLongClicked = viewModel::deleteEvent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: HomeState,
    interactions: HomeInteractions
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_home)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dark,
                    titleContentColor = light
                ),
                modifier = Modifier.padding(bottom = 5.dp)
            )
        },
        floatingActionButton = {
           FloatingActionButton(
               onClick = interactions.onAddEventClicked,
               containerColor = dark
           ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null
                )
           }
        }
    ) { innerPadding ->
        when (val dialog = state.dialog) {
            is Dialog.ConfirmSuppression -> AlertDialog(
                title = {
                    Text(text = stringResource(id = R.string.title_confirmation))
                },
                text = {
                    Text(text = stringResource(id = R.string.message_confirmation_suppression_event, dialog.libelle))
                },
                onDismissRequest = interactions.onDismissDialog,
                confirmButton = {
                    Button(onClick = interactions.onConfirmDialog) {
                        Text(text = stringResource(id = R.string.common_yes))
                    }
                },
                dismissButton = {
                    Button(onClick = interactions.onDismissDialog) {
                        Text(text = stringResource(id = R.string.common_no))
                    }
                }
            )
            Dialog.None -> { }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(background)
        ) {
            if (state.events.isNotEmpty()) {
                items(state.events) { event ->
                    event.Compose(
                        onEventClicked = interactions.onEventClicked,
                        onEventLongClicked = interactions.onEventLongClicked
                    )
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.label_no_event),
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
    }
}

@Composable
fun Event.Compose(
    onEventClicked: (Event) -> Unit,
    onEventLongClicked: (Event) -> Unit
) {
    val nbParticipants = nbParticipants ?: 0
    val kilometers = totalMeters?.div(1000f) ?: 0f
    CustomCard(
        title = name,
        leftText = stringResource(id = R.string.text_nb_participants, nbParticipants),
        rightText = if (kilometers == 0f) null else stringResource(id = R.string.text_nb_kilometres, kilometers),
        backgroundColor = if (isDone) done else light,
        modifier = Modifier.padding(horizontal = 10.dp),
        onClick = { onEventClicked(this) },
        onLongClick = { onEventLongClicked(this) }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    GuetteTesKilometresTheme {
        val events = persistentListOf(
            Event(
                id = 0,
                name = "100 kilomètres",
                isDone = true,
                totalMeters = 100000,
                nbParticipants = 28
            ),
            Event(
                id = 1,
                name = "24 heures",
                isDone = false,
                totalMeters = 0,
                nbParticipants = 0
            )
        )
        ScreenBody(
            state = HomeState(
                events = events,
                dialog = Dialog.ConfirmSuppression(libelle = "test"),
                idEventToDelete = null
            ),
            interactions = HomeInteractions(
                { }, { }, { }, { }, { }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeNoEventPreview() {
    GuetteTesKilometresTheme {
        val events = persistentListOf<Event>()
        ScreenBody(
            state = HomeState(
                events = events,
                dialog = Dialog.None,
                idEventToDelete = null
            ),
            interactions = HomeInteractions(
                { }, { }, { }, { }, { }
            )
        )
    }
}