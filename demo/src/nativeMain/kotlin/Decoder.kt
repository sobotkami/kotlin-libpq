/*
@file:OptIn(ExperimentalSerializationApi::class)

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.*
import org.postgresql.PGresult
import org.postgresql.PQgetvalue
import org.postgresql.PQntuples

fun <T> decodeFromPgResult(res: CPointer<PGresult>, deserializer: DeserializationStrategy<T>): T {
    val decoder = PgResultDecoder(res)
    return decoder.decodeSerializableValue(deserializer)
}

inline fun <reified T> decodePgResult(res: CPointer<PGresult>): T = decodeFromPgResult(res, serializer())


class PgResultDecoder(private val res: CPointer<PGresult>, var elementsCount: Int = 0) : Decoder, CompositeDecoder {
    private var elementIndex = 0
    private var rowIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        println("beginStructure($res, $elementsCount)")
        return PgResultDecoder(res, elementsCount)
    }

    override fun decodeBoolean(): Boolean = TODO("Not yet implemented: Boolean")
    override fun decodeByte(): Byte = TODO("Not yet implemented: Byte")
    override fun decodeChar(): Char = TODO("Not yet implemented: Char")
    override fun decodeDouble(): Double = TODO("Not yet implemented: Double")
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = TODO("Not yet implemented: Enum")
    override fun decodeFloat(): Float = TODO("Not yet implemented: Float")

    @ExperimentalSerializationApi
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder = TODO("Not yet implemented: Inline")
    override fun decodeInt(): Int = TODO("Not yet implemented: Int")
    override fun decodeLong(): Long = TODO("Not yet implemented: Long")

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = TODO("Not yet implemented: NotNullMark")

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = TODO("Not yet implemented: Null")
    override fun decodeShort(): Short = TODO("Not yet implemented: Short")
    override fun decodeString(): String = TODO("Not yet implemented: String")

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = decodeBoolean()
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = decodeByte()
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = decodeShort()
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = decodeInt()
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = decodeLong()

    override fun decodeInlineElement(
        descriptor: SerialDescriptor, index: Int
    ): Decoder = decodeInline(descriptor.getElementDescriptor(index))

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?
    ): T = decodeSerializableValue(deserializer)

    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?
    ): T? = TODO()

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = decodeFloat()
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = decodeDouble()
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        println("elementIndex: $elementIndex")
        return elementIndex++
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = decodeChar()
    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        return PQgetvalue(res, rowIndex++, index)!!.toKString()
    }

    override fun endStructure(descriptor: SerialDescriptor) {}

    // override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = PQntuples(res).also { elementsCount = it }
}

 */