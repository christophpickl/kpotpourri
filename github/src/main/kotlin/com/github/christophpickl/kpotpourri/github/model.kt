package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.common.KotlinNoArg
import java.io.File


@KotlinNoArg
internal annotation class JsonData

internal @JsonData data class MilestoneJson(
        val title: String,
        val number: Int,
        val state: String,
        val url: String
) {
    fun toMilestone() = Milestone(
            version = title,
            number = number,
            state = State.byJsonValue(state),
            url = url
    )
}

internal @JsonData data class IssueJson(
        val title: String,
        val number: Int,
        val state: String, // "open", "closed"
        val milestone: MilestoneJson? = null
) {
    fun toIssue() = Issue(
            title = title,
            number = number,
            state = State.byJsonValue(state),
            milestone = milestone?.toMilestone()
    )
}


internal @JsonData data class UpdateMilestone(
        val state: String
)

internal @JsonData data class UpdateMilestoneResponseJson(
        val state: String
)


internal @JsonData data class AssetUploadResponse(
        val name: String,
        val state: String,
        val size: Int
)

data class Issue(
        val title: String,
        val number: Int,
        val state: State,
        val milestone: Milestone?
)

data class Milestone(
        val version: String,
        val number: Int,
        val state: State,
        val url: String
)


@JsonData data class CreateReleaseRequest(
        val tag_name: String,
        val name: String,
        val body: String,
        val draft: Boolean = true,
        val prerelease: Boolean = false
)

@JsonData data class TagResponse(
        val name: String
)


enum class State(
        val jsonValue: String
) {
    Open("open"),
    Closed("closed");

    companion object {
        fun byJsonValue(seek: String) = State.values().first { it.jsonValue == seek }
    }
}

@JsonData data class CreateReleaseResponse(
        val id: Int,
        val url: String
)

data class AssetUpload(
        val releaseId: Int,
        val fileName: String,
        val contentType: String,
        val file: File
)
