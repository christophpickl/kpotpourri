package com.github.christophpickl.kpotpourri.github.non_test

import com.github.christophpickl.kpotpourri.github.AssetUpload
import com.github.christophpickl.kpotpourri.github.GithubConfig
import com.github.christophpickl.kpotpourri.github.buildGithub4k
import com.google.common.io.Files
import java.io.File


fun main(args: Array<String>) {
    val github = buildGithub4k(GithubConfig.testRepository)


//    val milestones = github.listOpenMilestones()
//    milestones.prettyPrint()
//    github.listIssues(milestones[0]).prettyPrint()

//    github.listTags().prettyPrint()

//    github.listReleases().prettyPrint()
    github.uploadReleaseAsset(AssetUpload(
            releaseId = 5934443,
            fileName = "fileName2.jar",
            // contentType = "text/plain",
            contentType = "application/java-archive",
            // bytes = ByteSource.wrap(byteArrayOf(0, 1, 1, 0))
            bytes = Files.asByteSource(File("/Users/wu/upload.jar"))
    ))
}
