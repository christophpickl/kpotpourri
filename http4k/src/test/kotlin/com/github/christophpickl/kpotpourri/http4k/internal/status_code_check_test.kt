package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.Http4kStatusException
import com.github.christophpickl.kpotpourri.http4k.Request4k
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.SC_404_NotFound
import com.github.christophpickl.kpotpourri.http4k.StatusCheckMode
import com.github.christophpickl.kpotpourri.http4k.StatusCheckResult
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.testDummy
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test class StatusCodeCheckTest {

    private val notSetAtAll = StatusCheckMode.NotSetAtAll
    private val disabled = StatusCheckMode.Anything
    private val enforced1 = StatusCheckMode.Enfore(1)
    private val enforced2 = StatusCheckMode.Enfore(2)
    private val enforced = enforced1
    private val custom = StatusCheckMode.Custom({ _, _ -> StatusCheckResult.Ok })

    @DataProvider
    fun nullIfEmptyProvider(): Array<Array<out Any?>> = arrayOf(
            arrayOf(notSetAtAll, notSetAtAll, notSetAtAll),
            arrayOf(notSetAtAll, enforced, enforced),
            arrayOf(enforced, disabled, disabled),
            arrayOf(enforced, notSetAtAll, enforced),
            arrayOf(enforced1, enforced2, enforced2),
            arrayOf(enforced, custom, custom)
    )

    @Test(dataProvider = "nullIfEmptyProvider")
    fun `properChecker`(global: StatusCheckMode, request: StatusCheckMode, expected: StatusCheckMode) {
        assertThat(
                requestScopeGoesBeforeGlobalScope(global, request),
                equalTo(expected as Any))
    }


    fun `checkStatusCode - global not set, request enforce family 4, when 200, Then throw exception`() {
        assertThrown<Http4kStatusException>({ e -> e.message == "Status code 200 expected to be of group 4!"}) {
            checkStatusCode(
                    global = StatusCheckMode.NotSetAtAll,
                    request = StatusCheckMode.EnforceFamily(StatusFamily.ClientError_4),
                    request4k = Request4k.testDummy,
                    response4k = Response4k.testDummy.copy(statusCode = SC_200_Ok))
        }
    }

    fun `checkStatusCode - global not set, request enforce family 4, when 404, Then Ok`() {
        checkStatusCode(
                global = StatusCheckMode.NotSetAtAll,
                request = StatusCheckMode.EnforceFamily(StatusFamily.ClientError_4),
                request4k = Request4k.testDummy,
                response4k = Response4k.testDummy.copy(statusCode = SC_404_NotFound))
    }
}
