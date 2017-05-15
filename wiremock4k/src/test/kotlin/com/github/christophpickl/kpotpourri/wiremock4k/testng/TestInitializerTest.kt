package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.testng.TestInitializer.exjectOverridePort
import com.github.christophpickl.kpotpourri.wiremock4k.testng.TestInitializer.injectMockUrl
import com.github.christophpickl.kpotpourri.wiremock4k.testng.TestInitializer.injectPort
import org.testng.annotations.Test
import kotlin.reflect.KVisibility

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "unused")
@Test class InjectMockPortTest {

    companion object {
        private val anyInteger = Integer(0)
        private val port = 42
        private val anyPort = 999
    }


    fun `sunshine`() {
        class DtoSunshine {
            @InjectMockPort private lateinit var port: Integer
            val publicPort get() = port
        }

        val dto = DtoSunshine()

        injectPort(dto, port)

        dto.publicPort shouldMatchValue port
    }

    fun `must be private `() {
        class DtoNotPrivate {
            @InjectMockPort lateinit var port: Integer
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("port", KVisibility.PRIVATE.toString(), KVisibility.PUBLIC.toString())) {
            injectPort(DtoNotPrivate(), anyPort)
        }
    }

    fun `not late init`() {
        class DtoNotLateInit {
            @InjectMockPort private var port: Integer = anyInteger
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("port", "lateinit")) {
            injectPort(DtoNotLateInit(), anyPort)
        }
    }

    // not checking for val (instead of var), as lateinit works on var only (and will be checked before hand anyway)
    // @InjectMockPort private !lateinit! val port: Integer = anyInteger

    fun `not Integer`() {
        class DtoNotInteger {
            @InjectMockPort private lateinit var port: String
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("port", Integer::class.java.name, String::class.java.name)) {
            injectPort(DtoNotInteger(), anyPort)
        }
    }

}

@Suppress("unused")
@Test class InjectMockUrlTest {

    companion object {
        private val anyUrl = "anyUrl"
    }

    fun `sunshine`() {
        class DtoSunshine {
            @InjectMockUrl private lateinit var url: String
            val publicUrl get() = url
        }

        val dto = DtoSunshine()

        injectMockUrl(dto, "url")

        dto.publicUrl shouldMatchValue "url"
    }

    fun `not String`() {
        class DtoNotString {
            @InjectMockUrl private lateinit var notString: ArrayList<Int>
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("notString", String::class.java.name, ArrayList::class.java.name)) {
            injectMockUrl(DtoNotString(), anyUrl)
        }
    }

    fun `must be private `() {
        class DtoNotPrivate {
            @InjectMockUrl lateinit var notPrivate: String
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("notPrivate", KVisibility.PRIVATE.toString(), KVisibility.PUBLIC.toString())) {
            injectMockUrl(DtoNotPrivate(), anyUrl)
        }
    }

    fun `not late init`() {
        class DtoNotLateInit {
            @InjectMockUrl private var notLateInit: String = anyUrl
        }

        assertThrown<TestInitializationException>(expectedMessageParts = listOf("notLateInit", "lateinit")) {
            injectMockUrl(DtoNotLateInit(), anyUrl)
        }
    }
}

@Test class OverrideMockPortTest {

    companion object {
        private const val port = 42
    }

    fun `sunshine`() {
        @OverrideMockPort(port) class DtoSunshine

        exjectOverridePort(DtoSunshine()) shouldMatchValue port
    }

    fun `not defined returns null`() {
        class DtoNoPort

        exjectOverridePort(DtoNoPort()) shouldMatchValue null
    }
}
