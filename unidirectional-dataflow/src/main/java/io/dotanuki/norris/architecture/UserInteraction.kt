package io.dotanuki.norris.architecture

interface UserInteraction {
    object OpenedScreen : UserInteraction
    object RequestedFreshContent : UserInteraction
}