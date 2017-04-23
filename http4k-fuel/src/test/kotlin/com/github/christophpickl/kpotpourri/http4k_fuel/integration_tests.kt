package com.github.christophpickl.kpotpourri.http4k_fuel

import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.integration_tests.AuthIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.BaseUrlIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.GetRequestsIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.HeadersIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.PostAndCoIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.QueryParamsIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.StatusCodeCheckIT
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import org.testng.annotations.Test

val clientProducer = { FuelHttpClient(MetaMap()) }

@Test class AuthApacheIT : AuthIT(clientProducer)
@Test class BaseUrlApacheIT : BaseUrlIT(clientProducer)
@Test class GetRequestsApacheIT : GetRequestsIT(clientProducer)
@Test class HeadersApacheIT : HeadersIT(clientProducer)
@Test class PostAndCoApacheIT : PostAndCoIT(clientProducer)
@Test class QueryParamsApacheIT : QueryParamsIT(clientProducer)
@Test class StatusCodeCheckApacheIT : StatusCodeCheckIT(clientProducer)

fun main(args: Array<String>) {
    buildHttp4k {
//        customProperty(500)
    }
}
