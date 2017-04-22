package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.github.internal.AssetUploadResponse
import com.github.christophpickl.kpotpourri.github.internal.IssueJson
import com.github.christophpickl.kpotpourri.github.internal.MilestoneJson
import com.github.christophpickl.kpotpourri.github.internal.UpdateMilestoneRequestJson
import com.github.christophpickl.kpotpourri.github.internal.UpdateMilestoneResponseJson
import com.github.christophpickl.kpotpourri.http4k.HttpProtocol
import com.github.christophpickl.kpotpourri.http4k.StatusFamily
import com.github.christophpickl.kpotpourri.http4k.UrlConfig
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k

// https://developer.github.com/v3/


fun buildGithub4k(config: GithubConfig): GithubApi {
    return GithubApiImpl(config)
}

interface GithubApi {
    fun listOpenMilestones(): List<Milestone>
    fun listIssues(milestone: Milestone): List<Issue>
    fun listTags(): List<Tag>

    fun close(milestone: Milestone)
    fun listReleases(): List<CreateReleaseResponse>
    fun createNewRelease(createRequest: CreateReleaseRequest): CreateReleaseResponse
    fun uploadReleaseAsset(upload: AssetUpload)
}

class GithubApiImpl(
        private val config: GithubConfig,
        private val protocol: HttpProtocol = HttpProtocol.Https,
        private val hostName: String = "api.github.com",
        private val port: Int = 443
) : GithubApi {

    private val log = LOG {}

    private val baseUrlConfig: UrlConfig = UrlConfig(
            protocol = protocol,
            hostName = hostName,
            port = port,
            path = "/repos/${config.repositoryOwner}/${config.repositoryName}"
    )

    companion object {
        private val GITHUB_MIMETYPE = "application/vnd.github.v3+json"
    }

//    private val mapper = ObjectMapper()
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val http4k = buildHttp4k {
        baseUrlBy(baseUrlConfig)
        basicAuth(config.username, config.password)
        addHeader("Accept" to GITHUB_MIMETYPE)
        enforceStatusFamily(StatusFamily.Success_2)
    }


    override fun listOpenMilestones(): List<Milestone> {
        log.debug("listOpenMilestones()")
        // state defaults to "open"
        return http4k.get("/milestones", Array<MilestoneJson>::class)
                .map { it.toMilestone() }
                .sortedBy { it.version }
    }

    override fun listIssues(milestone: Milestone): List<Issue> {
        log.debug("listIssues(milestone={})", milestone)
        return http4k.get("/issues", Array<IssueJson>::class) {
            addQueryParam("state" to "all")
            addQueryParam("milestone" to milestone.number)
        }
                .map { it.toIssue() }
                .sortedBy { it.number }
    }

    /**
     * GET /repos/:owner/:repo/tags
     *
     * https://developer.github.com/v3/repos/#list-tags
     */
    override fun listTags() =
            http4k.get("/tags", Array<Tag>::class)
                    .toList()
                    .sortedBy { it.name }

    /**
     * PATCH /repos/:owner/:repo/milestones/:number
     *
     * https://developer.github.com/v3/issues/milestones/#update-a-milestone
     */
    override fun close(milestone: Milestone) {
        if (milestone.state == State.Closed) {
            throw Github4kException("Milestone already closed: $milestone")
        }
        val response = http4k.patch(
                url = "/milestones/${milestone.number}",
                jacksonObject = UpdateMilestoneRequestJson(state = State.Closed.jsonValue),
                returnType = UpdateMilestoneResponseJson::class) {
        }
        if (response.state != State.Closed.jsonValue) {
            throw Github4kException("Failed to close milestone: $milestone")
        }
    }

    /**
     * POST /repos/:owner/:repo/releases
     *
     * https://developer.github.com/v3/repos/releases/#create-a-release
     */
    override fun createNewRelease(createRequest: CreateReleaseRequest) =
            http4k.post("/releases", createRequest, CreateReleaseResponse::class)

    /**
     * GET /repos/:owner/:repo/releases
     *
     * https://developer.github.com/v3/repos/releases/#list-releases-for-a-repository
     */
    override fun listReleases(): List<CreateReleaseResponse> =
            http4k.get("/releases", Array<CreateReleaseResponse>::class)
                    .toList()
                    .sortedBy { it.name }

    /**
     * POST https://<upload_url>/repos/:owner/:repo/releases/:id/assets?name=foo.zip
     *
     * https://developer.github.com/v3/repos/releases/#upload-a-release-asset
     */
    override fun uploadReleaseAsset(upload: AssetUpload) {
        // "upload_url": "https://uploads.github.com/repos/christophpickl/gadsu_release_playground/releases/5934443/assets{?name,label}",

        val uploadUrl = http4k.get("/releases/${upload.releaseId}", SingleReleaseJson::class).upload_url.removeSuffix("{?name,label}")
        log.debug { "Upload URL for github assets: $uploadUrl" }
        // "https://uploads.github.com/repos/christophpickl/gadsu_release_playground/releases/5934443/assets{?name,label}"

        val response = http4k.post(uploadUrl, AssetUploadResponse::class) {
            addHeader("Content-Type" to upload.contentType)
            addQueryParam("name" to upload.fileName)
            requestBytesBody(upload.contentType, upload.bytes)
            disableBaseUrl()
        }

        log.debug { "Uploaded asset: $response" }
        if (response.state != "uploaded") {
            throw Github4kException("Upload failed for ${upload.fileName}! ($upload, $response)")
        }
    }
    data class SingleReleaseJson(
            val upload_url: String
    )

}
