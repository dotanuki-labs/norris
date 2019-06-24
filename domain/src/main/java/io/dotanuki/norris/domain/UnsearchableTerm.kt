package io.dotanuki.norris.domain

object UnsearchableTerm : Throwable(
    "Search term can not be empty"
)
