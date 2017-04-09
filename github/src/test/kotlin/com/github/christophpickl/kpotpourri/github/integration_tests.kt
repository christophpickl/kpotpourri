package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.UrlConfig
import com.github.christophpickl.kpotpourri.wiremock4k.WIREMOCK_DEFAULT_URL
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*


class Github4kIntegrationTestes : WiremockTest() {

    private val mockEndpointRelativeUrl = "/github"
    private val mockEndpointAbsoluteUrl = WIREMOCK_DEFAULT_URL + mockEndpointRelativeUrl

    fun `listOpenMilestones OK`() {
        stubFor(get(urlEqualTo(mockEndpointRelativeUrl)))

        testee().listOpenMilestones()

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo(mockEndpointRelativeUrl))
                .withHeader("Authorization", equalTo("asdf"))
        )
    }

    private fun testee() = GithubApiImpl(
            GithubConfig(
                    repositoryOwner = "testOwner",
                    repositoryName = "testName",
                    username = "testUser",
                    password = "testPass"
            ),
            UrlConfig(
                    port = 8080
            )
    )

}
