package com.example.guetteteskilometres.ui.screens.newperson

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guetteteskilometres.R
import com.example.guetteteskilometres.ui.components.CustomField
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.dark
import com.example.guetteteskilometres.ui.theme.light
import com.example.guetteteskilometres.ui.theme.valid
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NewPersonScreen(
    navigations: NewPersonNavigations,
    viewModel: NewPersonViewModel,
    idEvent: Long?
) {
    val state = viewModel.state.collectAsState().value

    ScreenBody(
        state = state,
        interactions = NewPersonInteractions(
            onNameChanged = viewModel::updateName,
            onFirstNameChanged = viewModel::updateFirstname,
            onValidateClicked = { viewModel.validate(idEvent) },
            onBackClicked = { navigations.navigateUp() }
        )
    )

    LaunchedEffect(state) {
        viewModel.events.onEach {
            when (it) {
                NewPersonEvents.NavigateUp -> navigations.navigateUp()
                is NewPersonEvents.NavigateUpWithPerson -> navigations.navigateUpWithPerson(it.idPerson)
            }
        }.launchIn(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: NewPersonState,
    interactions: NewPersonInteractions
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_new_person)) },
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
            FloatingActionButton(
                onClick = interactions.onValidateClicked,
                containerColor = valid
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CustomField(
                idLabel = R.string.label_firstname,
                value = state.firstName,
                idErrorMessage = state.idErrorMessage,
                onValueChange = interactions.onFirstNameChanged
            )
            CustomField(
                idLabel = R.string.label_name,
                value = state.name,
                idErrorMessage = state.idErrorMessage,
                onValueChange = interactions.onNameChanged
            )
        }
    }
}

@Preview
@Composable
private fun NewPersonScreenPreview() {
    GuetteTesKilometresTheme {
        ScreenBody(
            state = NewPersonState(
                name = null,
                firstName = null,
                idErrorMessage = null
            ),
            interactions = NewPersonInteractions(
                { }, { }, { }, { }
            )
        )
    }
}