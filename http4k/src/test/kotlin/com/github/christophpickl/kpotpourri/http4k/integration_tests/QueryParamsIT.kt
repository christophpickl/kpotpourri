package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.test4k.skip
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest


abstract class QueryParamsIT (restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `When no param is set, Then url stays same`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

    fun `When query param is set in request, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl) {
            queryParams += "k" to "v"
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl?k=v"))
    }

    fun `When query param is set in globals, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k = buildCustomHttp4k {
            queryParams += "k" to "v"
        }

        http4k.get<Any>(mockEndpointUrl)

        verifyWiremockGet(MockRequest("$mockEndpointUrl?k=v"))
    }


    fun `When query param is set in globals and request, Then request overrides`() {
        val queryParamUrlSuffix = "?k=request"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            queryParams += "k" to "global"
        }

        http4k.get<Any>(mockEndpointUrl) {
            queryParams += "k" to "request"
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl$queryParamUrlSuffix"))
    }

    fun `Given query params in baseUrl, When query param is set, Then params are concatenated properly`() {
        val queryParamUrlSuffix = "?k1=v1&k2=v2"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k1=v1")
        }
        http4k.get<Any>("") {
            queryParams += "k2" to "v2"
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl$queryParamUrlSuffix"))
    }

    fun `Given query params in baseUrl and globals, When query param is set too, Then params are concatenated properly`() {
        val queryParamUrlSuffix = "?k1=v1&k2=v2&k3=v3"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k1=v1")
            queryParams += "k2" to "v2"
        }
        http4k.get<Any>("") {
            queryParams += "k3" to "v3"
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl$queryParamUrlSuffix"))
    }

    fun `Given same query param in baseUrl, globals and request, Then params are duplicated, MEH`() {
        skip("support baseUrl query param parsing and override properly") // MINOR support baseUrl query param parsing and override properly
        val queryParamUrlSuffix = "?k=request"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k=base")
            queryParams += "k" to "global"
        }
        http4k.get<Any>("") {
            queryParams += "k" to "request"
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl$queryParamUrlSuffix"))
    }

}
