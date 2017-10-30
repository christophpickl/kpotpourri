package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.github.internal.GithubApiImpl
import com.github.christophpickl.kpotpourri.http4k.ServerConfig

/**
 * Entry point to create a [GithubApi] instance.
 */
fun buildGithub4k(
        config: RepositoryConfig,
        connection: ServerConfig = GITHUB_COM
): GithubApi = GithubApiImpl(config, connection)

/**
 * Single interface providing any functionality
 */
interface GithubApi {

    /**
     * GET /repos/:owner/:repo/milestones
     */
    fun listOpenMilestones(): List<Milestone>

    /**
     * GET /repos/:owner/:repo/issues?milestone=X
     */
    fun listIssues(milestone: Milestone): List<Issue>

    /**
     * GET /repos/:owner/:repo/tags
     *
     * https://developer.github.com/v3/repos/#list-tags
     */
    fun listTags(): List<Tag>

    /**
     * PATCH /repos/:owner/:repo/milestones/:number
     *
     * https://developer.github.com/v3/issues/milestones/#update-a-milestone
     */
    fun close(milestone: Milestone)

    /**
     * GET /repos/:owner/:repo/releases
     *
     * https://developer.github.com/v3/repos/releases/#list-releases-for-a-repository
     */
    fun listReleases(): List<CreateReleaseResponse>

    /**
     * POST /repos/:owner/:repo/releases
     *
     * https://developer.github.com/v3/repos/releases/#create-a-release
     */
    fun createNewRelease(createRequest: CreateReleaseRequest): CreateReleaseResponse

    /**
     * POST https://<upload_url>/repos/:owner/:repo/releases/:id/assets?name=foo.zip
     *
     * https://developer.github.com/v3/repos/releases/#upload-a-release-asset
     */
    fun uploadReleaseAsset(upload: AssetUpload)

}

/**
 * Common exception type.
 */
class Github4kException(message: String, cause: Exception? = null)
    : KPotpourriException(message, cause)
