package io.dotanuki.norris.rest.model

sealed class RelatedCategory {
    class Available(val name: String) : RelatedCategory()
    object Missing : RelatedCategory()
}