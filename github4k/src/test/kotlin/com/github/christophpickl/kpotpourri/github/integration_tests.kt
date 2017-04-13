package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

private val testPort = 8082

@Test class Github4kIntegrationTestes : WiremockTest(testPort) {

    private val repositoryOwner = "testOwner"
    private val repositoryName = "testName"
    private val endpointPrefix = "/repos/$repositoryOwner/$repositoryName"


    fun `listOpenMilestones - request made and response parsed`() {
        givenWiremock(
                method = WiremockMethod.GET,
                path = "$endpointPrefix/milestones",
                statusCode = SC_200_Ok,
                body = """[{
                        "title": "jsonTitle",
                        "number": 1,
                        "state": "open",
                        "url": "jsonUrl"
                        }]"""
        )

        val milestones = testee().listOpenMilestones()

        verifyWiremockGet(MockRequest("$endpointPrefix/milestones", {
            withHeader("Authorization", equalTo("Basic dGVzdFVzZXI6dGVzdFBhc3M="))
        }))
        assertThat(milestones[0], com.natpryce.hamkrest.equalTo(
                Milestone("jsonTitle", 1, State.Open, "jsonUrl")))
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

}
