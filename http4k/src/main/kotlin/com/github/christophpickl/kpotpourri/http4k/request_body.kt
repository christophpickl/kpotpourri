package com.github.christophpickl.kpotpourri.http4k


interface RequestWithEntityOpts {
    var requestBody: RequestBody
}


// TODO TEST me
fun bodyDisabled() = None
fun bodyString(body: String) = StringBody(body)
fun bodyJson(jacksonObject: Any) = JsonBody(jacksonObject)


sealed class RequestBody

object None : RequestBody()

class StringBody(val stringEntity: String) : RequestBody()

class JsonBody(val jacksonEntity: Any) : RequestBody()
