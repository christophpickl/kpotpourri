package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.common.file.verifyExistsAndIsFile
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import com.google.common.io.ByteSource
import com.google.common.io.Files
import java.io.File


interface RequestWithEntityOpts {
    var requestBody: RequestBody

    fun requestBodyDisabled() { requestBody = None }
    fun requestBody(body: String) { requestBody = StringBody(body) }
    fun requestBody(jacksonObject: Any) { requestBody = JsonBody(jacksonObject) }
    fun requestBytesBody(contentType: String, bytes: ByteSource) {
        requestBody = BytesBody(contentType, bytes)
    }
    fun requestBytesBody(contentType: String, bytes: ByteArray) {
        requestBody = BytesBody(contentType, ByteSource.wrap(bytes))
    }
    fun requestFileBody(contentType: String, file: File) {
        file.verifyExistsAndIsFile()
        requestBody = BytesBody(contentType, Files.asByteSource(file))
    }

}


sealed class RequestBody {

    object None : RequestBody()

    class StringBody(val stringEntity: String) : RequestBody()

    class JsonBody(val jacksonEntity: Any) : RequestBody()

    class BytesBody(val contentType: String, val bytes: ByteSource) : RequestBody()
}


sealed class DefiniteRequestBody {
    class DefiniteStringBody(val string: String) : DefiniteRequestBody()
    class DefiniteBytesBody(val bytes: ByteSource) : DefiniteRequestBody()
}
