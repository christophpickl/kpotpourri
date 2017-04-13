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
//                TODO headers = listOf("X-HTTP-Method-Override" to "PATCH"), // HttpURLConnection hack which does not support PATCH method
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
     * POST https://<upload_url>/repos/:owner/:repo/releases/:id/assets?name=foo.zip
     *
     * https://developer.github.com/v3/repos/releases/#upload-a-release-asset
     */
    override fun uploadReleaseAsset(upload: AssetUpload) {
        val response = http4k.post("/releases/${upload.releaseId}/assets", AssetUploadResponse::class) {
            addHeader("Content-Type" to upload.contentType)
            addQueryParam("name" to upload.fileName)
//                FIXME requestBytes = upload.file.readBytes()
        }

        log.debug { "Uploaded asset: $response" }
        if (response.state != "uploaded") {
            System.err.println("Upload failed for ${upload.fileName}!!! ($upload, $response)")
        }
    }
    //    private fun <T> request(
//            method: HttpMethod4k,
//            url: String,
//            returnType: Class<T>,
//            queryParameters: List<Pair<String, Any?>>? = null,
//            headers: List<Pair<String, String>>? = null,
//            requestEntity: Any? = null,
//            requestBytes: ByteArray? = null
//    ): T {
//        val (_, response, result) = FuelManager.instance.request(method = method, path = url, param = queryParameters).apply {
//            if (requestEntity != null) {
//                body(mapper.writeValueAsString(requestEntity))
//            }
//            if (requestBytes != null) {
//                body(requestBytes)
//            }
//        }
//                .authenticate(config.username, config.password)
//                .header("Accept" to GITHUB_MIMETYPE)
//                .apply { if (headers !=null ) { httpHeaders.putAll(headers)} }
//                .responseString()
//
////        log.trace("Status code: {}", response.httpStatusCode)
//
//        result.fold({ success: String ->
//            return mapper.readValue(success, returnType)
//        }, { fail: FuelError ->
//            throw GadsuException("GitHub call failed for URL: $url with parameters: $queryParameters! (fuel says: $fail)", fail.exception)
//        })
//    }

}
