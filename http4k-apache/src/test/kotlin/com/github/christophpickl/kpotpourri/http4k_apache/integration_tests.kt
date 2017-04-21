package com.github.christophpickl.kpotpourri.http4k_apache

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

val apacheProducer = { ApacheHttpClientHttpImpl(MetaMap()) }

@Test class AuthApacheIT : AuthIT(apacheProducer)
@Test class BaseUrlApacheIT : BaseUrlIT(apacheProducer)
@Test class GetRequestsApacheIT : GetRequestsIT(apacheProducer)
@Test class HeadersApacheIT : HeadersIT(apacheProducer)
@Test class PostAndCoApacheIT : PostAndCoIT(apacheProducer)
@Test class QueryParamsApacheIT : QueryParamsIT(apacheProducer)
@Test class StatusCodeCheckApacheIT : StatusCodeCheckIT(apacheProducer)

fun main(args: Array<String>) {
    buildHttp4k {
        apacheConnectTimeout(500)
    }
}
