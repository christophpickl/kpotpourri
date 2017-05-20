package com.github.christophpickl.kpotpourri.http4k.non_test

import com.github.christophpickl.kpotpourri.common.reflection.ReflectorImpl
import com.github.christophpickl.kpotpourri.http4k.HttpMethod4k
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClient
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientFactory
import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientType
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.test4k.fail
import org.testng.annotations.Test
import kotlin.reflect.KClass

@Suppress("unused")
val Request4k.Companion.testDummy: Request4k get() = Request4k(
        method = HttpMethod4k.GET,
        url = "/my"
)

@Suppress("unused")
val Response4k.Companion.testDummy: Response4k get() = Response4k(
        statusCode = SC_200_Ok,
        bodyAsString = "response body"
)

@Test abstract class AbstractHttpClientFactoryDetectorTest<HC : HttpClient> {

    abstract protected val httpClientEnum: HttpClientType
    abstract protected val expectedType: KClass<HC>

    fun `should be instantiable`() {
        val clazz = ReflectorImpl().lookupClass(httpClientEnum.fqnToLookFor)
                ?: throw AssertionError("Expected class to be found for $httpClientEnum!")
        val factory = clazz.newInstance() as HttpClientFactory
        val client = factory.build(MetaMap())

        if (client.javaClass != expectedType.java) {
            fail("Expected client to be of type $expectedType but was: ${client.javaClass.simpleName}")
        }
    }

}
