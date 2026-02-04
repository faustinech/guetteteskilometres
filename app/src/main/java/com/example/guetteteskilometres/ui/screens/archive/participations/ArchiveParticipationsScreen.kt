package com.example.guetteteskilometres.ui.screens.archive.participations

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.data.model.ArchiveParticipation

@Composable
fun ArchiveParticipationsScreen(
    navigations: ArchiveParticipationsNavigations,
    viewModel: ArchiveParticipationsViewModel,
    idEvent: Long
) {
    val state by viewModel.state.collectAsState()
    
    viewModel.initialize(idEvent)
    
    ScreenBody(
        state = state,
        interactions = ArchiveParticipationsInteractions(
            onBackClicked = navigations.navigateUp,
            onFilterChanged = viewModel::updateFilter
        )
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: ArchiveParticipationsState,
    interactions: ArchiveParticipationsInteractions
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
        }
    ) { innerPadding ->
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
                    if (state.participations.isNotEmpty() || (state.participations.isEmpty() && state.filter.isNotEmpty())) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            val totalKilometers =
                                state.participations.sumOf { it.totalMeters } / 1000f
                            if (state.filter.isEmpty()) {
                                val nbPersons = state.participations.size
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
                }
            }
            if (state.participations.isNotEmpty()) {
                items(state.participations) { participation ->
                    participation.Compose()
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
}

@Composable
private fun ArchiveParticipation.Compose() {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = stringResource(id = R.string.text_nb_metres, totalMeters),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}