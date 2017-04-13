package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.wiremock4k.MockRequest
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockMethod.*
import com.github.christophpickl.kpotpourri.wiremock4k.WiremockTest
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

private val testPort = 8082

@Test class Github4kIntegrationTestes : WiremockTest(testPort) {

    private val repositoryOwner = "testOwner"
    private val repositoryName = "testName"
    private val endpointPrefix = "/repos/$repositoryOwner/$repositoryName"

    private val pathMilestones = "$endpointPrefix/milestones"

    fun `headers Authorization and Accept are always set`() {
        givenWiremock(GET, "$endpointPrefix/milestones", body = "[]")

        testee().listOpenMilestones() // any request will be sufficient

        verifyWiremockGet(MockRequest("$endpointPrefix/milestones", {
            withHeader("Authorization", equalTo("Basic dGVzdFVzZXI6dGVzdFBhc3M="))
            withHeader("Accept", equalTo("application/vnd.github.v3+json"))
        }))
    }

    fun `listOpenMilestones - request made and response parsed`() {
        val milestone = Milestone.testInstance
        givenWiremock(GET, pathMilestones, body = milestone.toJson().wrapJsonArrayBrackets())

        val milestones = testee().listOpenMilestones()

        verifyWiremockGet(MockRequest(pathMilestones))
        assertThat(milestones[0], com.natpryce.hamkrest.equalTo(milestone))
    }

    fun `listIssues - request made and response parsed`() {
        val milestone = Milestone.testInstance
        val issue = Issue.testInstance
        val requestPath = "$endpointPrefix/issues?milestone=${milestone.number}&state=all"
        givenWiremock(GET, requestPath, body = issue.toJson().wrapJsonArrayBrackets())

        val actualIssues = testee().listIssues(milestone)

        verifyWiremockGet(MockRequest(requestPath))
        assertThat(actualIssues.size, com.natpryce.hamkrest.equalTo(1))
        assertThat(actualIssues[0], com.natpryce.hamkrest.equalTo(issue))
    }

    fun `listIssues - sorted by number`() {
        val milestone = Milestone.testInstance
        val issues = listOf(
                Issue.testInstance.copy(number = 2),
                Issue.testInstance.copy(number = 1))
        val requestPath = "$endpointPrefix/issues?milestone=${milestone.number}&state=all"
        givenWiremock(GET, requestPath, body = issues.toIssuesJson())

        val actualIssues = testee().listIssues(milestone)

        assertThat(actualIssues.map { it.number }, com.natpryce.hamkrest.equalTo(listOf(1, 2)))
    }

    fun `listTags - request made and response parsed`() {
        val tag = Tag.testInstance
        val requestPath = "$endpointPrefix/tags"
        givenWiremock(GET, requestPath, body = tag.toJson().wrapJsonArrayBrackets())

        val actualTags = testee().listTags()

        verifyWiremockGet(MockRequest(requestPath))
        assertThat(actualTags.size, com.natpryce.hamkrest.equalTo(1))
        assertThat(actualTags[0], com.natpryce.hamkrest.equalTo(tag))
    }

    fun `listTags - sorted by name`() {
        val tags = listOf(
                Tag.testInstance.copy(name = "1.1"),
                Tag.testInstance.copy(name = "1.0"))
        val requestPath = "$endpointPrefix/tags"
        givenWiremock(GET, requestPath, body = tags.toTagsJson())

        val actualTags = testee().listTags()

        verifyWiremockGet(MockRequest(requestPath))
        assertThat(actualTags.map { it.name }, com.natpryce.hamkrest.equalTo(listOf("1.0", "1.1")))
    }

    fun `close milestone - sunshine`() {
        val milestone = Milestone.testInstance
        val requestPath = "$endpointPrefix/milestones/${milestone.number}"
        givenWiremock(PATCH, requestPath, body = """ { "state": "closed" } """)

        testee().close(milestone)

        verifyRequest(PATCH, requestPath) {
            withRequestBody(equalTo("""{"state":"closed"}"""))
        }
    }

    fun `close milestone - close already closed throws`() {
        val milestone = Milestone.testInstance
        val requestPath = "$endpointPrefix/milestones/${milestone.number}"
        givenWiremock(PATCH, requestPath, body = """ { "state": "open" } """)

        assertThrown<Github4kException> {
            testee().close(milestone)
        }
    }

    fun `createNewRelease - sunshine`() {
        val releaseRequest = CreateReleaseRequest.testInstance
        val releaseResponse = CreateReleaseResponse.testInstance
        val requestPath = "$endpointPrefix/releases"
        givenWiremock(POST, requestPath, body = releaseResponse.toJson())

        val response = testee().createNewRelease(releaseRequest)

        verifyPostRequest(MockRequest(requestPath, {
           withRequestBody(releaseRequest.toEqualJson())
        }))
        assertThat(response, com.natpryce.hamkrest.equalTo(releaseResponse))
    }

    /*
    fun uploadReleaseAsset(upload: AssetUpload)
     */
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
