package io.dotanuki.norris.architecture

object UnsupportedUserInteraction : Throwable(
    "Target view action is not mapped"
)