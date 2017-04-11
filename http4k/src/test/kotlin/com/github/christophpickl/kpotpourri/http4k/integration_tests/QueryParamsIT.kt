package com.github.christophpickl.kpotpourri.http4k.integration_tests


abstract class QueryParamsIT (restClient: RestClientProducer) : Http4kWiremockTest(restClient) {

    companion object {
        private val QUERY_KEY = "queryKey"
        private val QUERY_VAL = "queryVal"
        private val QUERY = QUERY_KEY to QUERY_VAL
    }

    fun `When no param is set, Then url stays same`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl)

        verifyGetRequest(mockEndpointUrl)
    }

    fun `When query param is set, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            queryParams += QUERY
        }

        verifyGetRequest("${mockEndpointUrl}?${QUERY.first}=${QUERY.second}")
    }

}
