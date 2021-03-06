package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.wiremock4k.request.verifyGetRequest


abstract class QueryParamsIT (restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `When no param is set, Then url stays same`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `When query param is set in request, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k.get<Any>(mockEndpointUrl) {
            queryParams += "k" to "v"
        }

        verifyGetRequest("$mockEndpointUrl?k=v")
    }

    fun `When query param is set in globals, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k = buildCustomHttp4k {
            queryParams += "k" to "v"
        }

        http4k.get<Any>(mockEndpointUrl)

        verifyGetRequest("$mockEndpointUrl?k=v")
    }


    fun `When query param is set in globals and request, Then request overrides`() {
        val expectedQueryParamUrlSuffix = "?k=request"
        givenGetMockEndpointUrl(path = mockEndpointUrl + expectedQueryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            queryParams += "k" to "global"
        }

        http4k.get<Any>(mockEndpointUrl) {
            queryParams += "k" to "request"
        }

        verifyGetRequest("$mockEndpointUrl$expectedQueryParamUrlSuffix")
    }

    fun `Given query params in baseUrl, When query param is set, Then params are concatenated properly`() {
        val expectedQueryParamUrlSuffix = "?k1=v1&k2=v2"
        givenGetMockEndpointUrl(path = mockEndpointUrl + expectedQueryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k1=v1")
        }
        http4k.get<Any>("") {
            queryParams += "k2" to "v2"
        }

        verifyGetRequest("$mockEndpointUrl$expectedQueryParamUrlSuffix")
    }

    fun `Given query params in baseUrl and globals, When query param is set too, Then params are concatenated properly`() {
        val expectedQueryParamUrlSuffix = "?k1=v1&k2=v2&k3=v3"
        givenGetMockEndpointUrl(path = mockEndpointUrl + expectedQueryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k1=v1")
            queryParams += "k2" to "v2"
        }
        http4k.get<Any>("") {
            queryParams += "k3" to "v3"
        }

        verifyGetRequest("$mockEndpointUrl$expectedQueryParamUrlSuffix")
    }

    fun `Given same query param in baseUrl, globals and request, Then request param should have precedence`() {
        val queryParamUrlSuffix = "?k=request"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k=base")
            queryParams += "k" to "global"
        }
        http4k.get<Any>("") {
            queryParams += "k" to "request"
        }

        verifyGetRequest("$mockEndpointUrl$queryParamUrlSuffix")
    }

    fun `Given same query param in baseUrl and globals, Then globals param should have precedence`() {
        val expectedQueryParamUrlSuffix = "?k=global"
        givenGetMockEndpointUrl(path = mockEndpointUrl + expectedQueryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k=base")
            queryParams += "k" to "global"
        }
        http4k.get<Any>("")

        verifyGetRequest("$mockEndpointUrl$expectedQueryParamUrlSuffix")
    }

    fun `Given same query param in baseUrl and request, Then request param should have precedence`() {
        val expectedQueryParamUrlSuffix = "?k=request"
        givenGetMockEndpointUrl(path = mockEndpointUrl + expectedQueryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?k=base")
        }
        http4k.get<Any>("") {
            queryParams += "k" to "request"
        }

        verifyGetRequest("$mockEndpointUrl$expectedQueryParamUrlSuffix")
    }

}
