package com.example.samplearchitecture.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class BaseActor<VI: BaseViewIntent, SI: BaseModelIntent, S: BaseViewState>: ViewModel() {

    protected abstract val initialState: S

    private val eventChannel = BroadcastChannel<SI>(capacity = Channel.BUFFERED)
    private val intentChannel = BroadcastChannel<VI>(capacity = Channel.CONFLATED)

    suspend fun processIntent(intent: VI) = intentChannel.send(intent)

    val singleEvent: Flow<SI> = eventChannel.asFlow()

    var viewState: MutableStateFlow<S>? = null
        private set

    open fun onCreate() {
        viewState = MutableStateFlow(initialState)
        intentChannel
            .asFlow()
            .handleIntent()
            .onEach { viewState?.value = it }
            .catch {  }
            .launchIn(viewModelScope)
    }

    abstract fun Flow<VI>.handleIntent(): Flow<S>

}