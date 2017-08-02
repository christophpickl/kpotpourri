package com.github.christophpickl.kpotpourri.github

import com.github.christophpickl.kpotpourri.github.internal.JsonData
import java.util.Objects

/**
 * A specific issue to work on :)
 */
data class Issue(
        val title: String,
        val number: Int,
        val state: State,
        val milestone: Milestone?
) {
    companion object // for test extensions
}

/**
 * A milestone/version.
 */
data class Milestone(
        val version: String,
        val number: Int,
        val state: State,
        val url: String
) {
    companion object // for test extensions
}

/**
 * Internal GitHub JSON object.
 */
@JsonData data class CreateReleaseRequest(
        val tag_name: String,
        val name: String,
        val body: String,
        val draft: Boolean = false,
        val prerelease: Boolean = false
) {
    companion object // for test extensions
}

/**
 * Internal GitHub JSON object.
 */
@JsonData data class CreateReleaseResponse(
        val id: Int,
        val url: String,

        val tag_name: String,
        val name: String,
        val body: String,
        val draft: Boolean,
        val prerelease: Boolean
) {
    companion object // for test extensions
}

/**
 * GIT tag name.
 */
@JsonData data class Tag(
        val name: String
) {
    companion object // for test extensions
}

/**
 * An issue or a milestone can be of either state.
 */
enum class State(
        val jsonValue: String
) {
    /** State not yet closed. */
    Open("open"),
    /** State closed. */
    Closed("closed");

    companion object {
        /** Read by internal JSON representation or fail. */
        fun byJsonValue(seek: String) = State.values().first { it.jsonValue == seek }
    }
}

/**
 * Upload an asset to a specific release.
 */
data class AssetUpload(
        val releaseId: Int,
        val fileName: String,
        /** See: https://www.iana.org/assignments/media-types/media-types.xhtml */
        val contentType: String,
        val bytes: ByteArray
) {
    companion object {} // for test extensions

    /** Check bytes array manually. */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is AssetUpload) {
            return false
        }
        return Objects.equals(releaseId, other.releaseId) &&
                Objects.equals(fileName, other.fileName) &&
                Objects.equals(contentType, other.contentType) &&
                bytes.contentEquals(other.bytes)
    }

    /** Check bytes array manually. */
    override fun hashCode() = Objects.hash(releaseId, fileName, contentType)
}
