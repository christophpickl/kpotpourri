package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.github.AssetUpload
import com.github.christophpickl.kpotpourri.github.CreateReleaseRequest
import com.github.christophpickl.kpotpourri.github.CreateReleaseResponse
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.Issue
import com.github.christophpickl.kpotpourri.github.Milestone
import com.github.christophpickl.kpotpourri.github.State
import com.github.christophpickl.kpotpourri.github.Tag
import com.github.christophpickl.kpotpourri.github.internal.AssetUploadResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.google.common.io.ByteSource
import java.io.File

private val log = LOG {}

fun String.wrapJsonArrayBrackets() = "[ $this ]"

val GithubConfig.Companion.testRepository get() = GithubConfig(
        repositoryName = "gadsu_release_playground",
        repositoryOwner = "christophpickl",
        username = "christoph.pickl@gmail.com",
        password = detectGithubPass()
)

val githubPassSysprop = "github.pass"
private fun detectGithubPass(): String {
    val sysprop = System.getProperty(githubPassSysprop, null)
    if (sysprop != null) {
        log.debug { "Detected github credentials via system property -D$githubPassSysprop" }
        return sysprop
    }
    val file = File(File(System.getProperty("user.home")), ".mygithub")
    if (!file.exists()) {
        throw KPotpourriException("Expected one of A) system variable -D$githubPassSysprop or B) file at ~/.mygithub")
    }
    log.debug { "Detected github credentials from ${file.absolutePath}" }
    return file.readText().trim()
}

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

val Tag.Companion.testInstance get() = Tag("testName")

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
        url = "testUrl",

        tag_name = "testTagName",
        name = "testName",
        body = "testBody",
        draft = true,
        prerelease = true
)

fun CreateReleaseResponse.toJson() = """{"id":$id,"url":"$url","tag_name":"$tag_name","name":"$name","body":"$body","draft":$draft,"prerelease":$prerelease}"""

val AssetUpload.Companion.testInstance get() = AssetUpload(
        releaseId = 1,
        fileName = "testFileName",
        contentType = "content/type",
        bytes = ByteSource.wrap(byteArrayOf(0, 1, 1, 0))
)
internal val AssetUploadResponse.Companion.testInstance get() = AssetUploadResponse(
        name = "testName",
        state = "testeState",
        size = 42
)

internal fun AssetUploadResponse.toJson() = """{"name":"$name","state":"$state","size":$size}"""
