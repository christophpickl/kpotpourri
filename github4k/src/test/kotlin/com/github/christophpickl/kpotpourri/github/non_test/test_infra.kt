package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.github.AssetUpload
import com.github.christophpickl.kpotpourri.github.CreateReleaseRequest
import com.github.christophpickl.kpotpourri.github.CreateReleaseResponse
import com.github.christophpickl.kpotpourri.github.Issue
import com.github.christophpickl.kpotpourri.github.Milestone
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.State
import com.github.christophpickl.kpotpourri.github.Tag
import com.github.christophpickl.kpotpourri.github.detectGithubPass
import com.github.christophpickl.kpotpourri.github.internal.AssetUploadResponse
import com.github.christophpickl.kpotpourri.jackson4k.asString
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import mu.KotlinLogging.logger

private val log = logger {}

private val mapper = buildJackson4kMapper()

fun String.wrapJsonArrayBrackets() = "[ $this ]"

@Suppress("unused")
val RepositoryConfig.Companion.testRepository
    get() = RepositoryConfig(
            repositoryName = "gadsu_release_playground",
            repositoryOwner = "christophpickl",
            username = "christoph.pickl@gmail.com",
            password = detectGithubPass()
    )

@Suppress("unused")
val RepositoryConfig.Companion.testInstance
    get() = RepositoryConfig(
            repositoryName = "testRepoName",
            repositoryOwner = "testRepoOwner",
            username = "test@github.com",
            password = "testPwd"
    )
@Suppress("unused")
val RepositoryConfig.Companion.testInstance1
    get() = RepositoryConfig(
            repositoryName = "testRepoName1",
            repositoryOwner = "testRepoOwner1",
            username = "test@github.com1",
            password = "testPwd1"
    )
@Suppress("unused")
val RepositoryConfig.Companion.testInstance2
    get() = RepositoryConfig(
            repositoryName = "testRepoName2",
            repositoryOwner = "testRepoOwner2",
            username = "test@github.com2",
            password = "testPwd2"
    )


@Suppress("unused")
val Milestone.Companion.testInstance
    get() = Milestone(
            version = "1.0",
            number = 1,
            state = State.Open,
            url = "testUrl"
    )

fun Milestone.toJson() = """{
    "title": "$version",
    "number": $number,
    "state": "${state.jsonValue}",
    "url": "$url"
}"""

@Suppress("unused")
val Issue.Companion.testInstance
    get() = Issue(
            title = "testTitle",
            number = 1,
            state = State.Open,
            milestone = null
    )

fun Issue.toJson() = """{
    "title": "$title",
    "number": $number,
    "state": "${state.jsonValue}",
    "milestone": ${milestone?.toJson() ?: "null"}
}"""

fun List<Issue>.toIssuesJson() = map(Issue::toJson).joinToString().wrapJsonArrayBrackets()

@Suppress("unused")
val Tag.Companion.testInstance
    get() = Tag("testName")

fun Tag.toJson() = """{
    "name": "$name"
}"""

fun List<Tag>.toTagsJson() = map(Tag::toJson).joinToString().wrapJsonArrayBrackets()


@Suppress("unused")
val CreateReleaseRequest.Companion.testInstance
    get() = CreateReleaseRequest(
            tag_name = "testTagName",
            name = "testName",
            body = "testBody",
            draft = true,
            prerelease = true
    )

fun CreateReleaseRequest.toJson() = mapper.asString(this)
fun CreateReleaseRequest.toEqualJson(): StringValuePattern = equalTo(toJson())

@Suppress("unused")
val CreateReleaseResponse.Companion.testInstance
    get() = CreateReleaseResponse(
            id = 1,
            url = "testUrl",

            tag_name = "testTagName",
            name = "testName",
            body = "testBody",
            draft = true,
            prerelease = true
    )

fun CreateReleaseResponse.toJson() = mapper.asString(this)

@Suppress("unused")
val AssetUpload.Companion.testInstance
    get() = AssetUpload(
            releaseId = 1,
            fileName = "testFileName.txt",
            contentType = "content/type",
            bytes = byteArrayOf(0, 1, 1, 0)
    )

@Suppress("unused")
internal val AssetUploadResponse.Companion.testInstance
    get() = AssetUploadResponse(
            name = "testName",
            state = "testeState",
            size = 42
    )

internal fun AssetUploadResponse.toJson() = """{"name":"$name","state":"$state","size":$size}"""
