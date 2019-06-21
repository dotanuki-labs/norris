package io.dotanuki.norris.domain.model

sealed class RelatedCategory {
    class Available(val name: String) : RelatedCategory()
    object Missing : RelatedCategory()
}