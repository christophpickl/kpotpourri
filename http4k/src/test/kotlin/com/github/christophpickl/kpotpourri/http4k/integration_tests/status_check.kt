package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.Http4kStatusCodeException
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusException
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_500_InternalError
import com.github.christophpickl.kpotpourri.http4k.StatusCheckFail
import com.github.christophpickl.kpotpourri.http4k.StatusCheckOk
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.test4k.assertThrown


class StatusCodeCheckIT : Http4kWiremockTest() {

    fun `Given global status check, When request status check also, Then request takes precedence`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)
        val http4k = buildHttp4k {
            baseUrlBy(mockBaseUrl)
            enforceStatusCode(SC_200_Ok)
        }

        http4k.get(mockEndpointUrl) {
            enforceStatusCode(SC_500_InternalError)
        }
    }

    fun `Given status 500, When check is disabled, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        http4k.get(mockEndpointUrl) {
            disableStatusCheck()
        }
    }

    fun `Given status 200, When check enforced to 200, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            enforceStatusCode(SC_200_Ok)
        }
    }

    fun `Given status 500, When check enforced to 200, Then throw exception`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        assertThrown<Http4kStatusCodeException>({ e -> e.expected == SC_200_Ok && e.actual == SC_500_InternalError }) {
            http4k.get(mockEndpointUrl) {
                enforceStatusCode(SC_200_Ok)
            }
        }
    }

    fun `Given custom status check, When check succeeds, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            customStatusCheck { _, _ ->
                StatusCheckOk
            }
        }
    }

    fun `Given custom status check, When check fails, Then throw exception`() {
        val exceptionMessage = "testMessage"
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        assertThrown<Http4kStatusException>({ e -> e.message == exceptionMessage }) {
            http4k.get(mockEndpointUrl) {
                customStatusCheck { _, _ ->
                    StatusCheckFail(exceptionMessage)
                }
            }
        }
    }

    fun `Given status family check, When check succeeds, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            enforceStatusFamily(StatusFamily.Success_2)
        }
    }

    fun `Given status family check, When check fails, Then throw exception`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        assertThrown<Http4kStatusException> {
            http4k.get(mockEndpointUrl) {
                enforceStatusFamily(StatusFamily.Success_2)
            }
        }
    }

}
