package com.github.christophpickl.kpotpourri.http4k_tests

import com.github.christophpickl.kpotpourri.http4k.integration_tests.AuthIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.BaseUrlIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.GetRequestsIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.HeadersIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.PostAndCoIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.QueryParamsIT
import com.github.christophpickl.kpotpourri.http4k.integration_tests.StatusCodeCheckIT
import com.github.christophpickl.kpotpourri.http4k_apache.ApacheHttpClientHttpImpl
import org.testng.annotations.Test

val apacheProducer = { ApacheHttpClientHttpImpl() }

@Test class AuthApacheIT : AuthIT(apacheProducer)
@Test class BaseUrlApacheIT : BaseUrlIT(apacheProducer)
@Test class GetRequestsApacheIT : GetRequestsIT(apacheProducer)
@Test class HeadersApacheIT : HeadersIT(apacheProducer)
@Test class PostAndCoApacheIT : PostAndCoIT(apacheProducer)
@Test class QueryParamsApacheIT : QueryParamsIT(apacheProducer)
@Test class StatusCodeCheckApacheIT : StatusCodeCheckIT(apacheProducer)
