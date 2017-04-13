package com.github.christophpickl.kpotpourri.http4k_tests

import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.integration_tests.*
import com.github.christophpickl.kpotpourri.http4k.internal.MetaMap
import com.github.christophpickl.kpotpourri.http4k_apache.ApacheHttpClientHttpImpl
import com.github.christophpickl.kpotpourri.http4k_apache.apacheConnectTimeout
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
