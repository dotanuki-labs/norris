package io.dotanuki.norris.search

sealed class RelatedCategory {
    data class Available(val name: String) : RelatedCategory()
    object Missing : RelatedCategory()
}
