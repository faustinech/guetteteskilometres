package com.example.guetteteskilometres.ui.screens.events

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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.darkGreen
import com.example.guetteteskilometres.ui.theme.lightGreen
import com.example.guetteteskilometres.ui.theme.lightRed
import com.example.guetteteskilometres.ui.theme.primaryRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditEventDialog(
    onValidateClicked: () -> Unit,
    onDismissClicked: () -> Unit,
    onFieldChanged: (EventField, String?) -> Unit,
    onAscendingChanged: (Boolean) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
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
                        if (state.idEvent == null) {
                            R.string.title_create_event
                        } else R.string.title_edit_event
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = state.name.orEmpty(),
                    onValueChange = { onFieldChanged(EventField.Name, it) },
                    placeholder = {
                        Text(
                            text = "${stringResource(R.string.label_name_event)} *",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = {
                        if (!state.name.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onFieldChanged(EventField.Name, null)
                                }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                if (!state.alreadyStart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.label_descending),
                            textAlign = TextAlign.End
                        )
                        Switch(
                            checked = state.ascending ?: true,
                            onCheckedChange = onAscendingChanged,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = stringResource(R.string.label_ascending)
                        )
                    }
                    if (state.ascending != true) {
                        OutlinedTextField(
                            value = state.startMeters.orEmpty(),
                            onValueChange = { onFieldChanged(EventField.StartMeters, it) },
                            placeholder = {
                                Text(
                                    text = "${stringResource(R.string.label_start_meters)} *",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            trailingIcon = {
                                if (!state.startMeters.isNullOrEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            onFieldChanged(EventField.StartMeters, null)
                                        }
                                    )
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                if (state.idEvent != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.label_archiver),
                            textAlign = TextAlign.End
                        )
                        Switch(
                            checked = state.active ?: true,
                            onCheckedChange = onActiveChanged,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = stringResource(R.string.label_active)
                        )
                    }
                }
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
                                    if (state.idEvent == null) {
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
            }
        }
    )
}

@Preview
@Composable
fun PreviewEventDialog() {
    GuetteTesKilometresTheme {
        CreateOrEditEventDialog(
            onValidateClicked = { },
            onDismissClicked = { },
            onFieldChanged = { _, _-> },
            onAscendingChanged = { },
            onActiveChanged = { },
            state = Dialog.Input(
                idEvent = null,
                name = "100 kilomètres",
                startMeters = null,
                ascending = null,
                active = null,
                alreadyStart = true
            )
        )
    }
}