package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.github.internal.JsonData
import java.io.File



data class Issue(
        val title: String,
        val number: Int,
        val state: State,
        val milestone: Milestone?
) {
    companion object // for test extensions
}

data class Milestone(
        val version: String,
        val number: Int,
        val state: State,
        val url: String
) {
    companion object // for test extensions
}

@JsonData data class CreateReleaseRequest(
        val tag_name: String,
        val name: String,
        val body: String,
        val draft: Boolean = true,
        val prerelease: Boolean = false
) {
    companion object // for test extensions
}

@JsonData data class CreateReleaseResponse(
        val id: Int,
        val url: String
) {
    companion object // for test extensions
}

@JsonData data class Tag(
        val name: String
) {
    companion object // for test extensions
}


enum class State(
        val jsonValue: String
) {
    Open("open"),
    Closed("closed");

    companion object {
        fun byJsonValue(seek: String) = State.values().first { it.jsonValue == seek }
    }
}


data class AssetUpload(
        val releaseId: Int,
        val fileName: String,
        /** See: https://www.iana.org/assignments/media-types/media-types.xhtml */
        val contentType: String,
        val file: File
)
