package io.dotanuki.norris.architecture

sealed class ViewState<out T> {

    sealed class Loading<T> : ViewState<T>() {
        object FromEmpty : Loading<Nothing>()
        data class FromPrevious<T>(val previous: T) : Loading<T>()
    }

    data class Success<T>(val value: T) : ViewState<T>()
    data class Failed(val failed: Throwable) : ViewState<Nothing>()
}