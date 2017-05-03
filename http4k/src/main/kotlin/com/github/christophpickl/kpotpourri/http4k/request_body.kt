package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.file.verifyExistsAndIsFile
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import java.io.File
import java.util.Objects

internal val DEFAULT_CONTENT_TYPE: String = "*/*"
internal val DEFAULT_TEXT_CONTENT_TYPE: String = "text/plain"
internal val DEFAULT_JSON_CONTENT_TYPE: String = "application/json"

/**
 * Options object for setting the request body.
 */
interface RequestWithEntityOpts {

    /** Assign the request body yourself. */
    var requestBody: RequestBody

    /** Shortcut method to disable request boyd. */
    fun requestBodyDisabled() {
        requestBody = None
    }

    /**
     * Depending on passed body runtime type, determines the proper request body or defaults to a JSON object.
     *
     * @param contentType override default content type (if body is not of type Unit).
     */
    fun requestBody(body: Any, contentType: String? = null) {
        if (body is Unit) {
            requestBodyDisabled()
        } else if (body is String) {
            requestBody = StringBody(body, contentType ?: DEFAULT_TEXT_CONTENT_TYPE)
        } else if (body is File) {
            requestFileBody(body, contentType ?: DEFAULT_CONTENT_TYPE)
        } else if (body is ByteArray) {
            requestBytesBody(body, contentType ?: DEFAULT_CONTENT_TYPE)
        } else if (body is Number) {
            requestBody = StringBody(body.toString(), contentType ?: DEFAULT_TEXT_CONTENT_TYPE)
        } else {
            // MINOR support more restrict mode, where any marshalled JSON object has to be annotated.
            requestBody = JsonBody(body, contentType ?: DEFAULT_JSON_CONTENT_TYPE)
        }
    }

    /**
     * Read contents of file.
     */
    fun requestFileBody(file: File, contentType: String = DEFAULT_CONTENT_TYPE) {
        file.verifyExistsAndIsFile()
        requestBody = BytesBody(file.readBytes(), contentType)
    }

    /**
     * Provide raw binary data via request.
     */
    fun requestBytesBody(bytes: ByteArray, contentType: String = DEFAULT_CONTENT_TYPE) {
        requestBody = BytesBody(bytes, contentType)
    }

}

/**
 * External representation of a configured request body.
 */
sealed class RequestBody {

    /**
     * By default no body is set.
     */
    object None : RequestBody()

    /**
     * Plain text.
     */
    data class StringBody(val stringEntity: String, val contentType: String = DEFAULT_TEXT_CONTENT_TYPE) : RequestBody()

    /**
     * Any passed JSON object will be marshalled internally via Jackson.
     */
    data class JsonBody(val jacksonEntity: Any, val contentType: String = DEFAULT_JSON_CONTENT_TYPE) : RequestBody()

    /**
     * Raw binary request with custom content type.
     */
    data class BytesBody(val bytes: ByteArray, val contentType: String) : RequestBody() {

        /** Check bytes array manually. */
        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is BytesBody) {
                return false
            }
            return contentType == other.contentType && bytes.contentEquals(other.bytes)
        }

        /** Check bytes array manually. */
        override fun hashCode() = Objects.hash(contentType, bytes)
    }
}

/**
 * Internal representation of a configured request body (got no representation for disabled request body).
 *
 * Does not define the content type as it already has been set on a higher abstraction level.
 */
sealed class DefiniteRequestBody {

    /**
     * String payload.
     */
    data class DefiniteStringBody(val string: String) : DefiniteRequestBody()

    /**
     * Raw binary payload.
     */
    data class DefiniteBytesBody(val bytes: ByteArray) : DefiniteRequestBody() {

        /** Check bytes array manually. */
        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is DefiniteBytesBody) {
                return false
            }
            return bytes.contentEquals(other.bytes)
        }

        /** Check bytes array manually. */
        override fun hashCode() = bytes.hashCode()
    }

}
