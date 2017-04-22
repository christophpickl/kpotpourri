package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.test4k.SOME_KEY1
import com.github.christophpickl.kpotpourri.test4k.SOME_KEY2
import com.github.christophpickl.kpotpourri.test4k.SOME_PAIR
import com.github.christophpickl.kpotpourri.test4k.SOME_PAIR2
import com.github.christophpickl.kpotpourri.test4k.SOME_VAL1
import com.github.christophpickl.kpotpourri.test4k.SOME_VAL2
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest


abstract class QueryParamsIT (restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `When no param is set, Then url stays same`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl)

        verifyWiremockGet(MockRequest(mockEndpointUrl))
    }

    fun `When query param is set, Then url changed`() {
        givenGetMockEndpointUrl()

        http4k.get(mockEndpointUrl) {
            queryParams += SOME_PAIR
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl?${SOME_PAIR.first}=${SOME_PAIR.second}"))
    }

    fun `Given query params in baseUrl, When query param is set, Then params are concatenated properly`() {
        val queryParamUrlSuffix = "?$SOME_KEY1=$SOME_VAL1&$SOME_KEY2=$SOME_VAL2"
        givenGetMockEndpointUrl(path = mockEndpointUrl + queryParamUrlSuffix)

        http4k = buildCustomHttp4k {
            // http://localhost:8042 -- /mock -- query params1
            baseUrlBy("$wiremockBaseUrl$mockEndpointUrl?$SOME_KEY1=$SOME_VAL1")
        }
        http4k.get("") {
            queryParams += SOME_PAIR2 // add query params2
        }

        verifyWiremockGet(MockRequest("$mockEndpointUrl$queryParamUrlSuffix"))
    }

}
