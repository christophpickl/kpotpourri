package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k


data class GithubConfig(
        val repositoryOwner: String,
        val repositoryName: String,
        val username: String,
        val password: String
)

interface GithubApi {
    fun listOpenMilestones(): List<Milestone>
    fun listIssues(milestone: Milestone): List<Issue>
    fun listTags(): List<TagResponse>

    fun close(milestone: Milestone)
    fun createNewRelease(createRequest: CreateReleaseRequest): CreateReleaseResponse
    fun uploadReleaseAsset(upload: AssetUpload)
}

/**
 * https://developer.github.com/v3/
 */
class GithubApiImpl(
        private val config: GithubConfig
) : GithubApi {

    companion object {
        private val GITHUB_MIMETYPE = "application/vnd.github.v3+json"
    }

//    private val log = LOG(javaClass)
//    private val mapper = ObjectMapper()
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val http4k = buildHttp4k {
        baseUrlBy("https://api.github.com/repos/${config.repositoryOwner}/${config.repositoryName}")
        withDefaults {
            //            FIXME header += "accept" to GITHUB_MIMETYPE
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
     * GET /repos/:owner/:repo/tags
     *
     * https://developer.github.com/v3/repos/#list-tags
     */
    override fun listTags() =
            http4k.get("/tags", Array<TagResponse>::class).toList().sortedBy { it.name }

    /**
     * POST https://<upload_url>/repos/:owner/:repo/releases/:id/assets?name=foo.zip
     *
     * https://developer.github.com/v3/repos/releases/#upload-a-release-asset
     *
     * @param contentType see: https://www.iana.org/assignments/media-types/media-types.xhtml
     */
    override fun uploadReleaseAsset(upload: AssetUpload) {
        val response = http4k.post("/releases/${upload.releaseId}/assets", AssetUploadResponse::class) {
            headers += "content-type" to upload.contentType
            // TODO
//                queryParameters = listOf("name" to upload.fileName),
//                requestBytes = upload.file.readBytes()
        }

//        log.debug("Uploaded asset: {}", response)
        if (response.state != "uploaded") {
            System.err.println("Upload failed for ${upload.fileName}!!! ($upload, $response)")
        }
    }

    /**
     * PATCH /repos/:owner/:repo/milestones/:number
     *
     * https://developer.github.com/v3/issues/milestones/#update-a-milestone
     */
    override fun close(milestone: Milestone) {
        if (milestone.state == State.Closed) {
            throw KPotpourriException("Milestone already closed: $milestone")
        }
        // FIXME implement PATCH
//        val response = http4k.patch("/milestones/${milestone.number}", UpdateMilestoneResponseJson::class) {
//            bodyJson(UpdateMilestone(state = State.Closed.jsonValue))
////                TODO headers = listOf("X-HTTP-Method-Override" to "PATCH"), // HttpURLConnection hack which does not support PATCH method
//        }
//        assert(response.state == State.Closed.jsonValue)
    }

    // state defaults to "open"
    override fun listOpenMilestones() =
//        log.debug("listOpenMilestones()")
        http4k.get("/milestones", Array<MilestoneJson>::class)
                .map { it.toMilestone() }
                .sortedBy { it.version }


    override fun listIssues(milestone: Milestone) =
//        log.debug("listIssues(milestone={})", milestone)
        http4k.get("/issues", Array<IssueJson>::class)
//                TODO queryParameters = listOf(
//                        "state" to "all",
//                        "milestone" to milestone.number
//                ),
                .map { it.toIssue() }
                .sortedBy { it.number }


    /**
     * @param query parameters
     */
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
//                TODO .authenticate(config.username, config.password)
//                TODO .header("Accept" to GITHUB_MIMETYPE)
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
