package com.github.christophpickl.kpotpourri.http4k


interface RequestWithEntityOpts {
    var requestBody: RequestBody

    fun requestBodyDisabled() { requestBody = None}
    fun requestBody(body: String) { requestBody = StringBody(body)}
    fun requestBody(jacksonObject: Any) { requestBody = JsonBody(jacksonObject) }

}


sealed class RequestBody

object None : RequestBody()

class StringBody(val stringEntity: String) : RequestBody()

class JsonBody(val jacksonEntity: Any) : RequestBody()
