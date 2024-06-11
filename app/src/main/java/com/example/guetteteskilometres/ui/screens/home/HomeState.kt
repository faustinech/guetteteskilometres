package com.example.guetteteskilometres.ui.screens.home

import com.example.guetteteskilometres.data.model.Event
import kotlinx.collections.immutable.ImmutableList

data class HomeState(
    val events: ImmutableList<Event>
)