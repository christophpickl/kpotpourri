package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.matching.StringValuePattern

fun String.wrapJsonArrayBrackets() = "[ $this ]"

val GithubConfig.Companion.testRepository get() = GithubConfig(
        repositoryName = "gadsu_release_playground",
        repositoryOwner = "christophpickl",
        username = "christoph.pickl@gmail.com",
        password = System.getProperty("github.pass", null) ?: throw KPotpourriException("Expected system variable -Dgithub.pass")
)

val Milestone.Companion.testInstance get() = Milestone(
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

val Issue.Companion.testInstance get() = Issue(
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

val Tag.Companion.testInstance get() =  Tag("testName")

fun Tag.toJson() = """{
    "name": "$name"
}"""
fun List<Tag>.toTagsJson() = map(Tag::toJson).joinToString().wrapJsonArrayBrackets()


val CreateReleaseRequest.Companion.testInstance get() = CreateReleaseRequest(
        tag_name = "testTagName",
        name = "testName",
        body = "testBody",
        draft = true,
        prerelease = true
)
fun CreateReleaseRequest.toEqualJson(): StringValuePattern = equalTo("""{"tag_name":"$tag_name","name":"$name","body":"$body","draft":$draft,"prerelease":$prerelease}""")


val CreateReleaseResponse.Companion.testInstance get() = CreateReleaseResponse(
        id = 1,
        url = "testUrl"
)
fun CreateReleaseResponse.toJson() = """ { "id": $id, "url": "$url" } """
