package com.example.samplearchitecture.stub

import com.example.samplearchitecture.base.BaseActor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StubActor : BaseActor<StubViewIntent, StubModelIntent, StubViewState>() {

    override val initialState: StubViewState
        get() = StubViewState()

    override fun Flow<StubViewIntent>.handleIntent(): Flow<StubViewState> {
        return MutableStateFlow(initialState)
    }

}