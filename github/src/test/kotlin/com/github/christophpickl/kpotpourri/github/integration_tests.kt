package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

private val testPort = 8082

// FIXME enable me
@Test(enabled = false)
class Github4kIntegrationTestes : WiremockTest(testPort) {

    private val repositoryOwner = "testOwner"
    private val repositoryName = "testName"
    private val endpointPrefix = "/repos/$repositoryOwner/$repositoryName"

    fun `handle github error`() {
        stubFor(get(Endpoint.Milestones).willReturn(
                aResponse().withStatus(403)))

//        TODO assertThrown<> { }
        testee().listOpenMilestones() // just any call is sufficient

    }

    fun `listOpenMilestones - request made`() {
        stubFor(get(Endpoint.Milestones).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("[]")
        ))

        testee().listOpenMilestones()

        verify(WireMock.getRequestedFor(WireMock.urlEqualTo("$endpointPrefix/milestones"))
                .withHeader("Authorization", equalTo("Basic dGVzdFVzZXI6dGVzdFBhc3M="))
        )
    }

    fun `listOpenMilestones Response`() {
        stubFor(get(Endpoint.Milestones).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("""{
                        "title": "jsonTitle",
                        "number": 1,
                        "state": "open",
                        "url": "jsonUrl",
                        }""")
        ))

        val milestones = testee().listOpenMilestones()

        assertThat(milestones[0], com.natpryce.hamkrest.equalTo(
                Milestone("jsonTItle", 1, State.Open, "jsonUrl")))
    }


    private fun testee() = GithubApiImpl(
            config = GithubConfig(
                    repositoryOwner = repositoryOwner,
                    repositoryName = repositoryName,
                    username = "testUser",
                    password = "testPass"
            ),
            protocol = HttpProtocol.Http,
            hostName = "localhost",
            port = testPort
    )


    private enum class Endpoint(val path: String) {
        Milestones("/milestones")
    }

    private fun get(endpoint: Endpoint) =
            get(urlEqualTo(endpointPrefix + endpoint.path))

}
