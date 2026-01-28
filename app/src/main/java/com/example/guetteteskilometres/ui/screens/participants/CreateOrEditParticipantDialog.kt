package com.example.guetteteskilometres.ui.screens.participants

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.enums.SaveAlert
import com.example.guetteteskilometres.ui.theme.lightGreen
import com.example.guetteteskilometres.ui.theme.lightRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditParticipantDialog(
        state: ParticipantsState,
        onValidateClicked: () -> Unit,
        onDismissClicked: () -> Unit,
        onFieldChanged: (ParticipantField, String?) -> Unit,
        onActiveChanged: (Boolean) -> Unit,
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
                        if (state.idPerson == null) {
                            R.string.title_create_participant
                        } else R.string.title_edit_participant
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = state.firstname.orEmpty(),
                    onValueChange = { firstname -> onFieldChanged(ParticipantField.Firstname, firstname) },
                    placeholder = {
                        Text(
                            text = "${stringResource(R.string.label_firstname)} *",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = {
                        if (!state.firstname.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onFieldChanged(
                                        ParticipantField.Firstname,
                                        null
                                    )
                                }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = state.name.orEmpty(),
                    onValueChange = { name -> onFieldChanged(ParticipantField.Name, name) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.label_name),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = {
                        if (!state.name.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onFieldChanged(
                                        ParticipantField.Name,
                                        null
                                    )
                                }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = state.email.orEmpty(),
                    onValueChange = { email -> onFieldChanged(ParticipantField.Email, email) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.label_email),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    trailingIcon = {
                        if (!state.email.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onFieldChanged(
                                        ParticipantField.Email,
                                        null
                                    )
                                }
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_inactive)
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
                state.alert?.let {
                    when (it) {
                        SaveAlert.Error -> {
                            Text(
                                text = stringResource(R.string.message_error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        SaveAlert.MissingFields -> {
                            Text(
                                text = stringResource(R.string.message_error_missing_field),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        SaveAlert.Success -> {
                            Text(
                                text = stringResource(
                                    if (state.idPerson == null) {
                                        R.string.text_creation_ok
                                    } else R.string.text_edit_ok
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightGreen, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
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