# Wiremock4k

This is a small test library basically simplifying the [Wiremock](http://wiremock.org/) API.

It automates starting/stopping/reseting the wiremock server by providing a TestNG listener `WiremockTestngListener`
and a base class `WiremockTest`. Additionally the setup of responses and verification of requests are somehow made simpler.

## Via TestNG listener

This is the preferred approach, as it does not limit you in having only a single base class or a single test runner like in JUnit.

```kotlin
import com.github.christophpickl.kpotpourri.wiremock4k.DEFAULT_WIREMOCK_PORT
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_HOSTNAME
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTestngListener
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(WiremockTestngListener::class)
class MyWiremockTest {

    fun `verify request`() {
        givenWiremock(WiremockMethod.GET, "/rest")
        
        println("execute request: http://$WIREMOCK_HOSTNAME:$DEFAULT_WIREMOCK_PORT/rest")
        
        verifyGetRequest("/rest")
    }
    
}
```

## Via TestNG base class

Having a single inheritance language, this could lead to some problems if you are already in need of a certain base class.
On the other hand, using the base class offers you additional features like easily setting the port or direct access
to the wiremock server instance.

```kotlin
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyDeleteRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyPostRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyPutRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyRequest
import com.github.christophpickl.kpotpourri.wiremock4k.request.withCookie
import com.github.christophpickl.kpotpourri.wiremock4k.request.withHeader
import com.github.christophpickl.kpotpourri.wiremock4k.response.givenWiremock
import com.github.christophpickl.kpotpourri.wiremock4k.response.withHeaders
import com.github.christophpickl.kpotpourri.wiremock4k.testng.WiremockTest
import org.testng.annotations.Test

@Test class UsageTest : WiremockTest() {

    private val path = "/rest"

    fun `prepare response`() {
        givenWiremock(
                method = WiremockMethod.GET,
                path = path,
                statusCode = 200,
                responseBody = "hello wiremock4k"
        ) {
            // acting on `ResponseDefinitionBuilder`
            withHeaders("Accept" to "application/json")
        }
    }

    fun `verify request`() {
        println(wiremockBaseUrl) // http://localhost:9987

        verifyGetRequest(path)
        verifyPostRequest(path)
        verifyPutRequest(path)
        verifyDeleteRequest(path)

        verifyGetRequest(path) {
            // acting on `RequestPatternBuilder`
            withHeader("Accept", "application/json")
            withCookie("JESSIONID", "1234")
        }

        // or select HTTP method dynamically by passing an enum instance
        verifyRequest(WiremockMethod.PATCH, path)
    }

    fun `access the native mock server instance`() {
        println(server.isRunning)

        server.findNearMissesForUnmatchedRequests()
    }

}
```
