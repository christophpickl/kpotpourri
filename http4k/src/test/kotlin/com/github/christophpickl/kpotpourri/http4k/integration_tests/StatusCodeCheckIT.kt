package com.github.christophpickl.kpotpourri.http4k.integration_tests

import com.github.christophpickl.kpotpourri.http4k.Http4kStatusCodeException
import com.github.christophpickl.kpotpourri.http4k.Http4kStatusException
import com.github.christophpickl.kpotpourri.http4k.SCR_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SCR_418_Teapot
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_201_Created
import com.github.christophpickl.kpotpourri.http4k.SC_302_Found
import com.github.christophpickl.kpotpourri.http4k.SC_400_BadRequest
import com.github.christophpickl.kpotpourri.http4k.SC_418_Teapot
import com.github.christophpickl.kpotpourri.http4k.SC_500_InternalError
import com.github.christophpickl.kpotpourri.http4k.SR_2xx_Success
import com.github.christophpickl.kpotpourri.http4k.StatusCheckResult
import com.github.christophpickl.kpotpourri.http4k.StatusCode
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


abstract class StatusCodeCheckIT (restClient: HttpImplProducer) : Http4kWiremockTest(restClient) {

    fun `Given status 500, When global and request status check, Then request check takes precedence and no exception thrown`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)
        val http4k = buildHttp4k {
            baseUrlBy(wiremockBaseUrl)
            enforceStatusCheck(SC_200_Ok)
        }

        http4k.get(mockEndpointUrl) {
            enforceStatusCheck(SC_500_InternalError)
        }
    }

    fun `Given status 201, When global check wants 418 and no request check is set, Then throw exception`() {
        givenGetMockEndpointUrl(statusCode = SC_201_Created)

        val http4k = buildHttp4k {
            baseUrlBy(wiremockBaseUrl)
            enforceStatusCheck(SC_418_Teapot)
        }

        assertThrown<Http4kStatusCodeException> ({ e -> e.expected == SCR_418_Teapot && e.actual == SC_201_Created }) {
            http4k.get(mockEndpointUrl) {
                // DEFAULT: unsetStatusCheck()
            }
        }
    }

    fun `Given status 500, When check is disabled, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        http4k.get(mockEndpointUrl) {
            anyStatusCheck()
        }
    }

    fun `Given status 200, When check enforced to 200, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            enforceStatusCheck(SC_200_Ok)
        }
    }

    fun `Given status 500, When check enforced to 200, Then throw exception`() {
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        assertThrown<Http4kStatusCodeException>({ e -> e.expected == SCR_200_Ok && e.actual == SC_500_InternalError }) {
            http4k.get(mockEndpointUrl) {
                enforceStatusCheck(SC_200_Ok)
            }
        }
    }

    fun `Given custom status check, When check succeeds, Then do nothing`() {
        givenGetMockEndpointUrl(statusCode = SC_200_Ok)

        http4k.get(mockEndpointUrl) {
            customStatusCheck { _, _ ->
                StatusCheckResult.Ok
            }
        }
    }

    fun `Given custom status check, When check fails, Then throw exception`() {
        val exceptionMessage = "testMessage"
        givenGetMockEndpointUrl(statusCode = SC_500_InternalError)

        assertThrown<Http4kStatusException>({ e -> e.message == exceptionMessage }) {
            http4k.get(mockEndpointUrl) {
                customStatusCheck { _, _ ->
                    StatusCheckResult.Fail(exceptionMessage)
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


    @DataProvider
    fun provideInvalidStatusCodes(): Array<Array<out Any>> = arrayOf(
            arrayOf(SC_302_Found),
            arrayOf(SC_400_BadRequest),
            arrayOf(SC_500_InternalError)
    )

//    TODO test for SC_100_Continue/SC_301_Moved, as each http impl could behave differently
    // SC_100_Continue ... will let apache run into timeouts...
    @Test(dataProvider = "provideInvalidStatusCodes")
    fun `Given a status code of not family 2, Then exception is thrown`(someInvalidStatus: StatusCode) {
        givenGetMockEndpointUrl(statusCode = someInvalidStatus)

        assertThrown<Http4kStatusCodeException>({ e ->
            e.actual == someInvalidStatus && e.expected == SR_2xx_Success
        }) {
            buildCustomHttp4k { enforceStatusFamily(StatusFamily.Success_2) }.get(mockEndpointUrl)
        }
    }
}
