package io.dotanuki.norris.rest

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class RawSearch(
    val result: List<RawFact>
)

@Serializable
data class RawFact(
    val id: String,
    val url: String,
    val value: String,
    val categories: List<String>
)

@Serializable(with = RawCategoriesSerializer::class)
class RawCategories(
    val raw: List<String>
)

@OptIn(InternalSerializationApi::class)
internal object RawCategoriesSerializer : KSerializer<RawCategories> {

    private val serializer = ListSerializer(String.serializer())

    override val descriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: RawCategories) {
        encoder.encodeSerializableValue(serializer, value.raw)
    }

    override fun deserialize(decoder: Decoder) =
        RawCategories(
            decoder.decodeSerializableValue(serializer)
        )
}
