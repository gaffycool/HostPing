package com.app.host.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<T> : ViewModel() {

    abstract fun defaultState(): T

    protected val _uiState = MutableStateFlow(defaultState())
    val uiState: StateFlow<T> = _uiState

    private val _navEventChannel = Channel<NavEvent>(capacity = Channel.BUFFERED)
    val navEventFlow: Flow<NavEvent>
        get() = _navEventChannel.receiveAsFlow()

    protected fun newState(stateCopy: (T) -> T) {
        val oldState = _uiState.value!!
        _uiState.value = stateCopy(oldState)
    }

    protected fun requireState(block: (T) -> Unit): Unit = block(_uiState.value!!)

    protected fun requireState(): T = _uiState.value!!
}

sealed interface NavEvent {
    data class Navigate(val route: String) : NavEvent
}

@Composable
fun <T : Any> SingleNavEvent(
    sideEffectFlow: Flow<T>,
    lifeCycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow) {
        lifecycleOwner.repeatOnLifecycle(lifeCycleState) {
            sideEffectFlow.collect(collector)
        }
    }
}