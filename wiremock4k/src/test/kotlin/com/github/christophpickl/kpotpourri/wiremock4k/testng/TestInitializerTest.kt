package com.github.christophpickl.kpotpourri.wiremock4k.testng

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.github.christophpickl.kpotpourri.wiremock4k.testng.TestInitializer.injectPort
import org.testng.annotations.Test

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
@Test class TestInitializerTest {

    companion object {
        private val anyInteger = Integer(0)
        private val port = 42
        private val anyPort = 999
    }


    fun `sunshine`() {
        class DtoSunshine {
            @InjectPort private lateinit var port: Integer
            val publicPort get() = port
        }

        val dto = DtoSunshine()

        injectPort(dto, port)

        dto.publicPort shouldMatchValue port
    }

    fun `must be private `() {
        class DtoNotPrivate {
            @InjectPort lateinit var port: Integer
        }

        assertThrown<TestInitializationException> {
            injectPort(DtoNotPrivate(), anyPort)
        }
    }

    fun `not late init`() {
        class DtoNotLateInit {
            @InjectPort var port: Integer = anyInteger
        }

        assertThrown<TestInitializationException> {
            injectPort(DtoNotLateInit(), anyPort)
        }
    }

    fun `val`() {
        class DtoVal {
            @InjectPort val port: Integer = anyInteger
        }

        assertThrown<TestInitializationException> {
            injectPort(DtoVal(), anyPort)
        }
    }

    fun `not Integer`() {
        class DtoNotInteger {
            @InjectPort lateinit var port: String
        }

        assertThrown<TestInitializationException> {
            injectPort(DtoNotInteger(), anyPort)
        }
    }

}
