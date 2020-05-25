package com.example.samplearchitecture.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.*

abstract class BaseFragment<VI : BaseViewIntent, SI : BaseModelIntent, S : BaseViewState, VM : BaseActor<VI, SI, S>> : Fragment() {

    abstract val actor: VM

    abstract val navigator: BaseNavigator

    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layoutRes, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.attachFragmentManager(fragmentManager)
        actor.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            actor.singleEvent
                .onEach { handleSingleEvent(it) }
                .catch { }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            actor.viewState
                ?.onEach { render(it) }
                ?.catch {  }
                ?.collect()
        }

        intents()
            ?.onEach { actor.processIntent(it) }
            ?.launchIn(lifecycleScope)
    }

    open fun intents(): Flow<VI>? = null

    open fun handleSingleEvent(event: SI) {}

    open fun render(state: S) {}

    override fun onDestroy() {
        super.onDestroy()
        navigator.release()
    }

}