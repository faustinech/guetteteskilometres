package com.example.guetteteskilometres.ui.screens.newparticipation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.ui.components.CustomField
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.background
import com.example.guetteteskilometres.ui.theme.dark
import com.example.guetteteskilometres.ui.theme.done
import com.example.guetteteskilometres.ui.theme.light
import com.example.guetteteskilometres.ui.theme.valid
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NewParticipationScreen(
    navigations: NewParticipationNavigations,
    viewModel: NewParticipationViewModel,
    idEvent: Long?,
    idParticipation: Long?,
    idPerson: Long?,
    isLastParticipation: Boolean
) {
    val state = viewModel.state.collectAsState().value

    viewModel.initialize(
        idEvent = idEvent,
        idParticipation = idParticipation,
        isLastParticipation = isLastParticipation
    )

    if (idPerson != null) {
        viewModel.updatePerson(idPerson)
    }

    ScreenBody(
        state = state,
        interactions = NewParticipationInteractions(
            onPersonChanged = viewModel::updatePerson,
            onStartMetersChanged = viewModel::updateStartMeters,
            onEndMetersChanged = viewModel::updateEndMeters,
            onValidationClicked = viewModel::validate,
            onBackClicked = { navigations.navigateUp() },
            onAddPersonClicked = {
                idEvent?.let { id ->
                    navigations.navigateToNewPerson(id)
                }
            },
            onDeleteDismissClicked = viewModel::dialogDismiss,
            onDeleteParticipationClicked = viewModel::requestDelete,
            onDeleteConfirmationClicked = viewModel::delete,
            onClearEndMetersClicked = viewModel::clearEnd,
            onClearStartMetersClicked = viewModel::clearStart
        )
    )

    LaunchedEffect(state) {
        viewModel.events.onEach {
            when (it) {
                NewParticipationEvents.NavigateUp -> navigations.navigateUp()
            }
        }.launchIn(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: NewParticipationState,
    interactions: NewParticipationInteractions
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_new_participation)) },
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
            Row(
                modifier = Modifier
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (state.isLastInput) {
                    FloatingActionButton(
                        onClick = interactions.onDeleteParticipationClicked,
                        containerColor = done
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    }
                }
                FloatingActionButton(
                    onClick = interactions.onValidationClicked,
                    containerColor = valid,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null
                    )
                }
            }
        }
    ) { innerPadding ->
        when (state.dialog) {
            Dialog.ConfirmSuppressionParticipation -> AlertDialog(
                title = {
                    Text(text = stringResource(id = R.string.title_confirmation))
                },
                text = {
                    Text(text = stringResource(id = R.string.message_confirmation_suppression))
                },
                onDismissRequest = interactions.onDeleteDismissClicked,
                confirmButton = {
                    Button(onClick = interactions.onDeleteConfirmationClicked) {
                        Text(text = stringResource(id = R.string.common_yes))
                    }
                },
                dismissButton = {
                    Button(onClick = interactions.onDeleteDismissClicked) {
                        Text(text = stringResource(id = R.string.common_no))
                    }
                }
            )
            Dialog.None -> { }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        bottom = 10.dp,
                        start = 10.dp,
                        end = 10.dp,
                        top = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO : voir pour le focus du champ participant
                var expanded by remember { mutableStateOf(false) }
                val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(3f)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .clickable(
                                onClick = {
                                    expanded = if (state.persons.isNotEmpty()) {
                                        !expanded
                                    } else {
                                        false
                                    }
                                }
                            ),
                        value = state.libellePerson,
                        onValueChange = {
                            // Ne rien faire ?
                        },
                        label = { Text(text = stringResource(id = R.string.label_name_person)) },
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        expanded = if (state.persons.isNotEmpty()) {
                                            !expanded
                                        } else {
                                            false
                                        }
                                    }
                                )
                            )
                        },
                        isError = state.idPersonErrorMessage != null,
                        supportingText = {
                            if (state.idPersonErrorMessage != null) {
                                Text(
                                    text = stringResource(id = state.idPersonErrorMessage),
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        },
                        enabled = state.persons.isNotEmpty()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .exposedDropdownSize()
                            .background(background)
                            .padding(horizontal = 10.dp)
                    ) {
                        for (person in state.persons) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "${person.firstname} ${person.name.orEmpty()}")
                                },
                                onClick = {
                                    expanded = false
                                    interactions.onPersonChanged(person)
                                }
                            )
                        }
                    }
                }
                IconButton(
                    onClick = interactions.onAddPersonClicked,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null
                    )
                }
            }
            CustomField(
                idLabel = R.string.label_start_kilometres,
                value = state.startMeters?.toString(),
                idErrorMessage = state.idStartErrorMessage,
                onValueChange = interactions.onStartMetersChanged,
                keyboardType = KeyboardType.Number,
                isReadOnly = !state.isLastInput || !state.startCanBeModify,
                onClearFilterClicked = interactions.onClearStartMetersClicked
            )
            CustomField(
                idLabel = R.string.label_end_kilometres,
                value = state.endMeters?.toString(),
                idErrorMessage = state.idEndErrorMessage,
                onValueChange = interactions.onEndMetersChanged,
                keyboardType = KeyboardType.Number,
                isReadOnly = !state.isLastInput,
                onClearFilterClicked = interactions.onClearEndMetersClicked
            )
        }
    }
}

@Preview
@Composable
private fun NewParticipationScreenPreview() {
    GuetteTesKilometresTheme {
        ScreenBody(
            state = NewParticipationState(
                person = Person(
                    id = 1,
                    firstname = "Faustine",
                    name = null,
                    idEvent = 1
                ),
                event = null,
                startMeters = 0,
                endMeters = 1234,
                persons = persistentListOf(),
                idStartErrorMessage = null,
                idEndErrorMessage = null,
                idPersonErrorMessage = null,
                dialog = Dialog.None,
                libellePerson = "",
                isLastInput = false,
                startCanBeModify = true
            ),
            interactions = NewParticipationInteractions(
                { }, { }, { }, { }, { }, { }, { }, { }, { }, { }, { }
            )
        )
    }
}