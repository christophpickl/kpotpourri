package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest


abstract class QueryParamsIT (restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    companion object {
        private val QUERY_KEY = "queryKey"
        private val QUERY_VAL = "queryVal"
        private val QUERY = QUERY_KEY to QUERY_VAL
    }

    fun `When no param is set, Then url stays same`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

    fun `When query param is set, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            queryParams += QUERY
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl?${QUERY.first}=${QUERY.second}"))
    }

    // MINOR check for query param already set in baseUrl

}
