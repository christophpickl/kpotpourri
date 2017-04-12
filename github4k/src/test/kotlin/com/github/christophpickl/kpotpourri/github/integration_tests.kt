package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

private val testPort = 8082

@Test class Github4kIntegrationTestes : WiremockTest(testPort) {

    private val repositoryOwner = "testOwner"
    private val repositoryName = "testName"
    private val endpointPrefix = "/repos/$repositoryOwner/$repositoryName"


    fun `listOpenMilestones - request made`() {
        givenWiremock(
                method = WiremockMethod.GET,
                statusCode = SC_200_Ok,
                body = "[]"
        )

        testee().listOpenMilestones()

        verifyWiremockGet(MockRequest("$endpointPrefix/milestones", {
            withHeader("Authorization", equalTo("Basic dGVzdFVzZXI6dGVzdFBhc3M="))
        }))
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
