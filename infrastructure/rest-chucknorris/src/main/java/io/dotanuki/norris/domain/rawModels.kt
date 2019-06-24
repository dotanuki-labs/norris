package io.dotanuki.norris.domain

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayClassDesc
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.StringSerializer

@Serializable
internal data class RawSearch(
    val result: List<RawFact>
)

@Serializable
internal data class RawFact(
    val id: String,
    val url: String,
    val value: String,
    val categories: List<String>
)

@Serializable(with = RawCategoriesSerializer::class)
internal class RawCategories(
    val raw: List<String>
)

internal object RawCategoriesSerializer : KSerializer<RawCategories> {

    private val serializer = ArrayListSerializer(StringSerializer)

    override val descriptor = ArrayClassDesc(serializer.descriptor)

    override fun serialize(encoder: Encoder, obj: RawCategories) {
        encoder.encodeSerializableValue(serializer, obj.raw)
    }

    override fun deserialize(decoder: Decoder) =
        RawCategories(
            decoder.decodeSerializableValue(serializer)
        )
}