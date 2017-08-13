package com.github.christophpickl.kpotpourri.http4k.internal

import com.fasterxml.jackson.core.type.TypeReference
import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.Http4kException
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.internal.ResponseCaster.cast
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.allOf
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.isA
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import kotlin.reflect.KClass

@Test
class ResponseCasterTest {

    fun `cast return class - primitives`() {
        assertReturnClass("body", Unit::class, Unit)
        assertReturnClass("", Unit::class, Unit)
        assertReturnClass("body", Any::class, response("body"))
        assertReturnClass("body", Response4k::class, response("body"))
        assertReturnClass("body", String::class, "body")
        assertReturnClass("", String::class, "")
        assertReturnClass("1.0", Float::class, 1.0F)
        assertReturnClass("1.0", Double::class, 1.0)
        assertReturnClass("1", Byte::class, 1.toByte())
        assertReturnClass("1", Short::class, 1.toShort())
        assertReturnClass("1", Int::class, 1)
        assertReturnClass("1", Long::class, 1.toLong())
        assertReturnClass("true", Boolean::class, true)
        assertReturnClass("1", Boolean::class, true)
    }

    @Test(dataProvider = "invalidNumberProvider")
    fun `cast return class - invalid number`(body: String, type: KClass<*>, ignore: Any) {
        assertThrown<NumberFormatException> {
            cast(response(body), ReturnOption.ReturnClass(type))
        }
    }

    fun `cast return class - invalid boolean`() {
        assertThrown<KPotpourriException> {
            cast(response("x"), ReturnOption.ReturnClass(Boolean::class))
        }
    }

    fun `cast return class - dto`() {
        assertReturnClass("""{ "name": "foo" }""", Dto::class, Dto("foo"))
        assertReturnClass("""{ "dtos": [ { "name": "foo" } ] }""", Dtos::class, Dtos(listOf(Dto("foo"))))
    }

    fun `cast return class - list fails`() {
        assertThrown<Http4kException>(listOf("TypeReference")) {
            cast(response("""[ { "name": "foo" } ]"""), ReturnOption.ReturnClass(List::class))
        }
    }

    fun `cast return type`() {
        assertReturnType("", Unit)
        assertReturnType<Any>("", response(""))
        assertReturnType("body", response("body"))
        assertReturnType("", response(""))
        assertReturnType("body", "body")
        assertReturnType("", "")
        assertReturnType("1.0", 1.0F)
        assertReturnType("1.0", 1.0)
        assertReturnType("1", 1.toByte())
        assertReturnType("1", 1.toShort())
        assertReturnType("1", 1)
        assertReturnType("1", 1.toLong())
        assertReturnType("1", true)
        assertReturnType("""{ "name": "foo" }""", Dto("foo"))
        assertReturnType("""{ "dtos": [ { "name": "foo" } ] }""", Dtos(listOf(Dto("foo"))))
        assertReturnType("""[ { "name": "foo" } ]""", listOf(Dto("foo")))
    }

    @Test(dataProvider = "invalidNumberProvider")
    fun <R : Any> `cast return type - invalid number`(body: String, ignore: Any, typeRef: TypeReference<R>) {
        assertThrown<NumberFormatException> {
            cast(response(body), ReturnOption.ReturnType(typeRef))
        }
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    @DataProvider
    fun invalidNumberProvider() = arrayOf(
            arrayOf("x", Float::class, typeOf<Float>()),
            arrayOf("x", Double::class, typeOf<Double>()),
            arrayOf("x", Byte::class, typeOf<Byte>()),
            arrayOf("x", Short::class, typeOf<Short>()),
            arrayOf("x", Int::class, typeOf<Int>()),
            arrayOf("x", Integer::class, typeOf<Integer>()),
            arrayOf("x", Long::class, typeOf<Long>())
    )

    private inline fun <reified R: Any> typeOf() = object : TypeReference<R>() {}

    private inline fun <reified R : Any> assertReturnType(body: String, expected: R) {
        assertThat(cast(response(body), ReturnOption.ReturnType(object : TypeReference<R>() {})),
                allOf(isA(R::class), equalTo(expected)))
    }

    private fun assertReturnClass(body: String, returnClass: KClass<*>, expected: Any) {
        assertThat(cast(response(body), ReturnOption.ReturnClass(returnClass)),
                allOf(isA(returnClass), equalTo(expected)))
    }

    private fun response(body: String) = Response4k(statusCode = SC_200_Ok, bodyAsString = body)

}

private data class Dto(
        val name: String
)

private data class Dtos(
        val dtos: List<Dto>
)
