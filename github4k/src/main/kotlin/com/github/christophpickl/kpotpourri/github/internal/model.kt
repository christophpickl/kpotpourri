package com.github.christophpickl.kpotpourri.github.internal

import com.github.christophpickl.kpotpourri.common.KotlinNoArg
import com.github.christophpickl.kpotpourri.github.Issue
import com.github.christophpickl.kpotpourri.github.Milestone
import com.github.christophpickl.kpotpourri.github.State


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
