package com.example.guetteteskilometres.ui.screens.participants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.Person
import com.example.guetteteskilometres.data.model.enums.ParticipantField
import com.example.guetteteskilometres.data.model.enums.ParticipantFilter
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.green
import com.example.guetteteskilometres.ui.theme.lightGreen
import com.example.guetteteskilometres.ui.theme.lightRed
import com.example.guetteteskilometres.ui.theme.secondaryRed
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ParticipantsScreen(
    navigations: ParticipantsNavigations,
    viewModel: ParticipantsViewModel
) {
    val state = viewModel.state.collectAsState().value

    viewModel.initialize()

    ScreenBody(
        state = state,
        interactions = ParticipantsInteractions(
            onBackClicked = navigations.navigateUp,
            onPersonClicked = { idPerson -> viewModel.updateDialog(idPerson) },
            onNewPersonClicked = { viewModel.updateDialog(idPerson = null) },
            onFilterSelected = { filter -> viewModel.updateActive(filter) },
            onFilterChanged = { filter -> viewModel.updateFilter(filter) },
            onValidateClicked = { viewModel.validate() },
            onActiveChanged = { active -> viewModel.updateActive(active) },
            onDismissClicked = { viewModel.dismissDialog() },
            onFieldChanged = { type, value -> viewModel.updateField(type, value)}
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ScreenBody(
    state: ParticipantsState,
    interactions: ParticipantsInteractions
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
                                text = stringResource(id = R.string.title_participants),
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
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.CenterEnd
            ) {
                FloatingActionButton(
                    onClick = { interactions.onNewPersonClicked() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.persons.isNotEmpty()) {
                stickyHeader {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FilterChips(
                            selectedFilter = state.activeFilter,
                            onFilterSelected = { chip -> interactions.onFilterSelected(chip) }
                        )
                        ParticipantSearchBar(
                            filter = state.filter,
                            onFilterChanged = { filter -> interactions.onFilterChanged(filter) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                items(state.persons) { person ->
                    PersonCard(person) { interactions.onPersonClicked(it) }
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.no_participant),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (state.isDialogVisible) {
            ParticipantAlertDialog(
                state = state,
                onValidateClicked = interactions.onValidateClicked,
                onDismissClicked = interactions.onDismissClicked,
                onFieldChanged = interactions.onFieldChanged,
                onActiveChanged = interactions.onActiveChanged
            )
        }
    }
}

@Composable
private fun ParticipantSearchBar(
    filter: String,
    onFilterChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = filter,
        onValueChange = onFilterChanged,
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
                modifier = Modifier.clickable { onFilterChanged("") }
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
private fun FilterChips(
    selectedFilter: ParticipantFilter,
    onFilterSelected: (ParticipantFilter) -> Unit
) {
    val filters = listOf(
        ParticipantFilter.All to stringResource(R.string.label_all),
        ParticipantFilter.Active to stringResource(R.string.label_active_plural),
        ParticipantFilter.Inactive to stringResource(R.string.label_inactive_plural)
    )
    LazyRow(
        modifier = Modifier.padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { (filter, label) ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParticipantAlertDialog(
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
                        ParticipantsAlert.Error -> {
                            Text(
                                text = stringResource(R.string.message_error),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        ParticipantsAlert.MissingField -> {
                            Text(
                                text = stringResource(R.string.message_error_missing_field),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(lightRed, RoundedCornerShape(5.dp))
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        ParticipantsAlert.Success -> {
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
                OutlinedButton(
                    onClick = onValidateClicked,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_valider)
                    )
                }
            }
        })

}

@Composable
private fun PersonCard(
    person: Person,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(person.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = when (person.name) {
                    null -> person.firstname
                    else -> "${person.firstname} ${person.name.uppercase()}"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = if (person.active) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = if (person.active) green else secondaryRed
            )
        }
    }
}

@Preview
@Composable
private fun ParticipantsAlertDialogPreview() {
    GuetteTesKilometresTheme {
        ParticipantAlertDialog(
            state = ParticipantsState(
                persons = persistentListOf(
                    Person(
                        id = 1,
                        name = "Charrette",
                        firstname = "Faustine",
                        email = null,
                        active = true
                    ),
                    Person(
                        id = 2,
                        name = "Ditto",
                        firstname = "Sarah",
                        email = null,
                        active = true
                    ),
                    Person(
                        id = 3,
                        name = "Rivaillon",
                        firstname = "Léo",
                        email = null,
                        active = false
                    )
                ),
                activeFilter = ParticipantFilter.All,
                filter = "",
                idPerson = null,
                firstname = null,
                name = null,
                email = null,
                active = null,
                alert = ParticipantsAlert.Error
            ),
            onDismissClicked = { },
            onValidateClicked = { },
            onFieldChanged = { _, _ -> },
            onActiveChanged = { }
        )
    }
}

@Preview
@Composable
private fun ParticipantsScreenPreview() {
    GuetteTesKilometresTheme {
        ScreenBody(
            state = ParticipantsState(
                persons = persistentListOf(
                    Person(
                        id = 1,
                        name = "Charrette",
                        firstname = "Faustine",
                        email = null,
                        active = true
                    ),
                    Person(
                        id = 2,
                        name = "Ditto",
                        firstname = "Sarah",
                        email = null,
                        active = true
                    ),
                    Person(
                        id = 3,
                        name = "Rivaillon",
                        firstname = "Léo",
                        email = null,
                        active = false
                    )
                ),
                activeFilter = ParticipantFilter.All,
                filter = "",
                idPerson = null,
                firstname = null,
                name = null,
                email = null,
                active = null
            ),
            interactions = ParticipantsInteractions(
                { },
                { },
                { },
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