package io.dotanuki.norris.rest.model

sealed class RelatedCategory {
    data class Available(val name: String) : RelatedCategory()
    object Missing : RelatedCategory()
}