package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.core.type.TypeReference
import com.github.christophpickl.kpotpourri.common.string.toBooleanLenient2
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.Response4k
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
internal object ResponseCaster {

    fun <R : Any> cast(response4k: Response4k, returnOption: ReturnOption<R>): R =
            when (returnOption) {
                is ReturnOption.ReturnClass<*> -> response4k.castTo(returnOption.klass) as R
                is ReturnOption.ReturnType<*> ->
                    if (returnOption.isSimpleType() || response4k.bodyAsString.isEmpty()) {
                        response4k.castTo((returnOption.typeRef.type as Class<R>).kotlin)
                    } else {
                        mapper.readValue<R>(response4k.bodyAsString, returnOption.typeRef)
                    }
            }

    private fun <R> ReturnOption.ReturnType<R>.isSimpleType() = when (typeRef.type as? Class<R>) {
        Any::class.java,
        Response4k::class.java,
        String::class.java,
        java.lang.String::class.java,
        Float::class.java,
        java.lang.Float::class.java,
        Double::class.java,
        java.lang.Double::class.java,
        Byte::class.java,
        java.lang.Byte::class.java,
        Short::class.java,
        java.lang.Short::class.java,
        Int::class.java,
        java.lang.Integer::class.java,
        Long::class.java,
        java.lang.Long::class.java,
        Boolean::class.java,
        java.lang.Boolean::class.java
        -> true
        else -> false
    }

    private fun <R : Any> Response4k.castTo(returnType: KClass<R>): R =
            when (returnType) {
                Response4k::class -> this as R
                String::class -> this.bodyAsString as R
                Any::class -> this as R
                Unit::class -> Unit as R
            // could catch parsing exceptions here ;)
                Float::class -> this.bodyAsString.toFloat() as R
                Double::class -> this.bodyAsString.toDouble() as R
                Byte::class -> this.bodyAsString.toByte() as R
            // ByteArray::class -> ??? as R
                Short::class -> this.bodyAsString.toShort() as R
                Int::class -> this.bodyAsString.toInt() as R
                Long::class -> this.bodyAsString.toLong() as R
                Boolean::class -> this.bodyAsString.toBooleanLenient2() as R
                else -> mapper.readValue(this.bodyAsString, returnType.java).apply {
                    if (this is ArrayList<*> && this.isNotEmpty() && this[0] is LinkedHashMap<*, *>) {
                        throw Http4kException("Seems as you ran into Java's type erasure problem! Use cast(response, ReturnOption.ReturnType instead to internally use Jackson's TypeReference!)")
                    }
                }
            }
}

internal sealed class ReturnOption<R> {
    class ReturnClass<R : Any>(val klass: KClass<R>) : ReturnOption<R>()
    class ReturnType<R>(val typeRef: TypeReference<R>) : ReturnOption<R>()
}

internal fun <R : Any> KClass<R>.toOption() = ReturnOption.ReturnClass<R>(this)
internal fun <R> TypeReference<R>.toOption() = ReturnOption.ReturnType<R>(this)
