package io.dotanuki.norris.features.utilties

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.ViewState
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

class ConfigChangesAwareStateContainer<T> : StateContainer<T>, ViewModel() {

    private val broadcaster by lazy {
        ConflatedBroadcastChannel<ViewState<T>>(ViewState.FirstLaunch)
    }

    override val emissionScope = viewModelScope

    override fun observableStates() = broadcaster.asFlow()

    override fun current(): ViewState<T> = broadcaster.value

    override suspend fun store(state: ViewState<T>) {
        broadcaster.send(state)
    }

    companion object {
        operator fun <T> invoke(host: FragmentActivity): StateContainer<T> {

            val factory = object : ViewModelProvider.Factory {
                override fun <Model : ViewModel> create(klass: Class<Model>) =
                    ConfigChangesAwareStateContainer<T>() as Model
            }

            val keyClazz = ConfigChangesAwareStateContainer::class.java
            return ViewModelProviders.of(host, factory)[keyClazz] as StateContainer<T>
        }
    }
}