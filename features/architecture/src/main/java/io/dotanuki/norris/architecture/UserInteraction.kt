package io.dotanuki.norris.architecture

sealed class UserInteraction {

    object OpenedScreen : UserInteraction()
    object RequestedFreshContent : UserInteraction()

    abstract class Feature : UserInteraction()
}