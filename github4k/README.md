# Github4k

High level SDK for accessing GitHub features using the [official ReST API](https://developer.github.com/v3/).

Following features are supported:

* List milestones/issues/tags/releases
* Close a milestone
* Create a new release
* Upload a release asset

```kotlin
import com.github.christophpickl.kpotpourri.github.AssetUpload
import com.github.christophpickl.kpotpourri.github.CreateReleaseRequest
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k

val github4k = buildGithub4k(
        config = RepositoryConfig(
                repositoryOwner = "repoOwner",
                repositoryName = "repoName",
                username = "user",
                password = "pass"
        )
)

val milestone = github4k.listOpenMilestones().first()
val issues = github4k.listIssues(milestone)
github4k.close(milestone)

val tag = github4k.listTags().first()
val release = github4k.createNewRelease(CreateReleaseRequest(
        tag_name = tag.name,
        name = "Release ${tag.name}",
        body = "Some description."))
github4k.uploadReleaseAsset(AssetUpload(
        releaseId = release.id,
        fileName = "release.zip",
        contentType = "application/zip",
        bytes = byteArrayOf(0, 1, 1, 0)
))
val releases = github4k.listReleases()
```
