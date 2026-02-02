package com.example.guetteteskilometres.ui.screens.participations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.ui.screens.participants.CreateOrEditParticipantDialog
import com.example.guetteteskilometres.ui.screens.participants.ParticipantField
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.darkGreen
import com.example.guetteteskilometres.ui.theme.lightGreen
import com.example.guetteteskilometres.ui.theme.lightRed
import com.example.guetteteskilometres.ui.theme.primaryRed
import com.example.guetteteskilometres.ui.theme.white
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditParticipationDialog(
    onDismissClicked: () -> Unit,
    onAddPersonClicked: () -> Unit,
    onPersonChanged: (Person) -> Unit,
    onFieldChanged: (ParticipationField, String?) -> Unit,
    onValidateClicked: () -> Unit,
    onValidateNewParticipantClicked: () -> Unit,
    onActiveChanged: (Boolean) -> Unit,
    onNewParticipantFieldChanged: (ParticipantField, String?) -> Unit,
    onNewParticipantsDismissClicked: () -> Unit,
    state: Dialog.Input,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismissClicked,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        content = {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(
                        if (state.person == null) {
                            R.string.title_create_participation
                        } else R.string.title_edit_participation
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TODO : voir pour le focus du champ participant
                    var expanded by remember { mutableStateOf(false) }
                    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1f)
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
                            value = state.person?.let { "${it.firstname} ${it.name.orEmpty()}" }.orEmpty(),
                            onValueChange = { },
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
                            shape = RoundedCornerShape(12.dp),
                            enabled = state.persons.isNotEmpty() || state.person != null
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .exposedDropdownSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 10.dp)
                        ) {
                            for (person in state.persons) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "${person.firstname} ${person.name.orEmpty()}")
                                    },
                                    onClick = {
                                        expanded = false
                                        onPersonChanged(person)
                                    }
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onAddPersonClicked,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    }
                }
                OutlinedTextField(
                    value = state.startMeters.orEmpty(),
                    onValueChange = { onFieldChanged(ParticipationField.StartMeters, it) },
                    placeholder = {
                        Text(
                            text = "${stringResource(R.string.label_start_kilometres)} *",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = state.endMeters.orEmpty(),
                    onValueChange = { onFieldChanged(ParticipationField.EndMeters, it) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.label_end_kilometres),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = {
                        if (!state.endMeters.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onFieldChanged(ParticipationField.EndMeters, null)
                                }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                state.alert?.let {
                    when (it) {
                        SaveAlert.Error -> {
                            Text(
                                text = stringResource(R.string.message_error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = primaryRed
                            )
                        }
                        SaveAlert.MissingFields -> {
                            Text(
                                text = stringResource(R.string.message_error_missing_field),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = primaryRed
                            )
                        }
                        SaveAlert.Incoherence -> {
                            Text(
                                text = stringResource(R.string.message_error_incoherent_field),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = primaryRed
                            )
                        }
                        SaveAlert.Success -> {
                            Text(
                                text = stringResource(
                                    if (state.idParticipation == null) {
                                        R.string.text_creation_ok
                                    } else R.string.text_edit_ok
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightGreen, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = darkGreen
                            )
                        }
                    }
                }
                if (state.alert != SaveAlert.Success) {
                    OutlinedButton(
                        onClick = onValidateClicked,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.button_valider)
                        )
                    }
                }

                state.createOrEditParticipantDialogInfos?.let {
                    CreateOrEditParticipantDialog(
                        state = it,
                        onValidateClicked = onValidateNewParticipantClicked,
                        onDismissClicked = onNewParticipantsDismissClicked,
                        onFieldChanged = onNewParticipantFieldChanged,
                        onActiveChanged = onActiveChanged
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewEventDialog() {
    GuetteTesKilometresTheme {
        CreateOrEditParticipationDialog(
            onDismissClicked = { },
            onAddPersonClicked = { },
            onPersonChanged = { },
            onFieldChanged = { _, _ -> },
            onValidateClicked = { },
            state = Dialog.Input(
                person = null,
                startMeters = "1234",
                endMeters = null,
                persons = persistentListOf(),
                alert = null,
                idParticipation = null,
                createOrEditParticipantDialogInfos = null
            ),
            onValidateNewParticipantClicked = { },
            onActiveChanged = { },
            onNewParticipantFieldChanged = { _, _ -> },
            onNewParticipantsDismissClicked = { }
        )
    }
}