package com.example.guetteteskilometres.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

open class BaseViewModel: ViewModel() {
    private val _didInitState = AtomicBoolean(false)
    val didInitState get() = _didInitState.get()

    protected fun launchInitState(initState: () -> Unit) {
        if (_didInitState.getAndSet(true)) return
        initState()
    }

    protected fun launchInitStateAsync(initState: suspend (() -> Unit)) = viewModelScope.launch {
        if (_didInitState.getAndSet(true)) return@launch
        initState()
    }
}