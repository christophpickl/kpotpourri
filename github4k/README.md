# Github4k

High level SDK for accessing GitHub features using the [official ReST API](https://developer.github.com/v3/).

```kotlin
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k

val github4k = buildGithub4k(GithubConfig(
    repositoryOwner = "repoOwner",
    repositoryName = "repoName",
    username = "user",
    password = "pass"
))

val milestone = github4k.listOpenMilestones().first()
val issues = github4k.listIssues(milestone)
println(issues.first().title)

// github4k.close(milestone)
// github4k.listTags()
// github4k.createNewRelease(..)
// github4k.uploadReleaseAsset(..)
```
