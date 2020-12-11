package io.dotanuki.norris.domain.model

sealed class RelatedCategory {
    data class Available(val name: String) : RelatedCategory()
    object Missing : RelatedCategory()
}
