package io.dotanuki.norris.facts

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

// Shamelessly ported from
// https://github.com/androidx/androidx/blob/androidx-main/lifecycle/lifecycle-runtime-ktx/src/main/java/androidx/lifecycle/RepeatOnLifecycle.kt
// so we don't need to depend on alpha releases of androidx extensions
// Reference : https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb

suspend fun Lifecycle.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state !== Lifecycle.State.INITIALIZED) {
        "repeatOnLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (currentState === Lifecycle.State.DESTROYED) {
        return
    }

    coroutineScope {
        withContext(Dispatchers.Main.immediate) {
            if (currentState === Lifecycle.State.DESTROYED) return@withContext

            var launchedJob: Job? = null
            var observer: LifecycleEventObserver? = null

            try {
                suspendCancellableCoroutine<Unit> { cont ->
                    val startWorkEvent = Lifecycle.Event.upTo(state)
                    val cancelWorkEvent = Lifecycle.Event.downFrom(state)
                    observer = LifecycleEventObserver { _, event ->
                        if (event == startWorkEvent) {
                            launchedJob = this@coroutineScope.launch(block = block)
                            return@LifecycleEventObserver
                        }
                        if (event == cancelWorkEvent) {
                            launchedJob?.cancel()
                            launchedJob = null
                        }
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            cont.resume(Unit) {}
                        }
                    }
                    this@repeatOnLifecycle.addObserver(observer as LifecycleEventObserver)
                }
            } finally {
                launchedJob?.cancel()
                observer?.let {
                    this@repeatOnLifecycle.removeObserver(it)
                }
            }
        }
    }
}

suspend fun LifecycleOwner.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
): Unit = lifecycle.repeatOnLifecycle(state, block)
