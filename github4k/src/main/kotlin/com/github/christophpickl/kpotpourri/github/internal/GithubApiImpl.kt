package com.github.christophpickl.kpotpourri.github.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.github.AssetUpload
import com.github.christophpickl.kpotpourri.github.CreateReleaseRequest
import com.github.christophpickl.kpotpourri.github.CreateReleaseResponse
import com.github.christophpickl.kpotpourri.github.Github4kException
import com.github.christophpickl.kpotpourri.github.GithubApi
import com.github.christophpickl.kpotpourri.github.Issue
import com.github.christophpickl.kpotpourri.github.Milestone
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.State
import com.github.christophpickl.kpotpourri.github.Tag
import com.github.christophpickl.kpotpourri.http4k.BaseUrlConfig
import com.github.christophpickl.kpotpourri.http4k.ServerConfig
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get
import com.github.christophpickl.kpotpourri.http4k.patch
import com.github.christophpickl.kpotpourri.http4k.post
import com.google.common.base.MoreObjects


@Suppress("KDocMissingDocumentation")
internal class GithubApiImpl(
        private val config: RepositoryConfig,
        connection: ServerConfig
) : GithubApi {

    companion object {
        private val GITHUB_MIMETYPE = "application/vnd.github.v3+json"
    }

    private val log = LOG {}

    private fun ServerConfig.toBaseUrlConfig() = BaseUrlConfig(
            protocol = protocol,
            hostName = hostName,
            port = port,
            path = "/repos/${config.repositoryOwner}/${config.repositoryName}"
    )

    private val http4k = buildHttp4k {
        baseUrlBy(connection.toBaseUrlConfig())
        basicAuth(config.username, config.password)
        addHeader("Accept" to GITHUB_MIMETYPE)
        enforceStatusFamily(StatusFamily.Success_2)
    }


    override fun listOpenMilestones(): List<Milestone> {
        log.debug("listOpenMilestones()")
        // state defaults to "open"
        return http4k.get<Array<MilestoneJson>>("/milestones")
                .map { it.toMilestone() }
                .sortedBy { it.version }
    }

    override fun listIssues(milestone: Milestone): List<Issue> {
        log.debug("listIssues(milestone={})", milestone)
        return http4k.get<Array<IssueJson>>("/issues") {
            addQueryParam("state" to "all")
            addQueryParam("milestone" to milestone.number)
        }
                .map { it.toIssue() }
                .sortedBy { it.number }
    }

    override fun listTags() =
            http4k.get<Array<Tag>>("/tags")
                    .toList()
                    .sortedBy(Tag::name)

    override fun close(milestone: Milestone) {
        if (milestone.state == State.Closed) {
            throw Github4kException("Milestone already closed: $milestone")
        }
        val response = http4k.patch<UpdateMilestoneResponseJson>("/milestones/${milestone.number}") {
            requestBody(UpdateMilestoneRequestJson(state = State.Closed.jsonValue))
        }
        if (response.state != State.Closed.jsonValue) {
            throw Github4kException("Failed to close milestone: $milestone")
        }
    }

    override fun createNewRelease(createRequest: CreateReleaseRequest) =
            http4k.post<CreateReleaseResponse>("/releases") {
                requestBody(createRequest)
            }

    override fun listReleases(): List<CreateReleaseResponse> =
            http4k.get<Array<CreateReleaseResponse>>("/releases")
                    .toList()
                    .sortedBy { it.name }

    override fun uploadReleaseAsset(upload: AssetUpload) {
        // "upload_url": "https://uploads.github.com/repos/christophpickl/gadsu_release_playground/releases/5934443/assets{?name,label}",

        val uploadUrl = http4k.get<SingleReleaseJson>("/releases/${upload.releaseId}").upload_url.removeSuffix("{?name,label}")
        log.debug { "Upload URL for github assets: $uploadUrl" }
        // "https://uploads.github.com/repos/christophpickl/gadsu_release_playground/releases/5934443/assets{?name,label}"

        val response = http4k.post<AssetUploadResponse>(uploadUrl) {
            addHeader("Content-Type" to upload.contentType)
            addQueryParam("name" to upload.fileName)
            requestBytesBody(upload.bytes, upload.contentType)
            disableBaseUrl()
        }

        log.debug { "Uploaded asset: $response" }
        if (response.state != "uploaded") {
            throw Github4kException("Upload failed for ${upload.fileName}! ($upload, $response)")
        }
    }

    override fun toString() = MoreObjects.toStringHelper(this)
            .add("config", config)
            .toString()

}

@JsonData internal data class SingleReleaseJson(
        val upload_url: String
)
