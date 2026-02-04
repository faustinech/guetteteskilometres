package com.example.guetteteskilometres.ui.screens.archive

data class ArchiveEventsInteractions(
    val onBackClicked: () -> Unit,
    val onEventClicked: (Long) -> Unit,
    val onFilterChanged: (String) -> Unit,
    val onExportClicked: (Long) -> Unit,
    val onDismissClicked: () -> Unit
)