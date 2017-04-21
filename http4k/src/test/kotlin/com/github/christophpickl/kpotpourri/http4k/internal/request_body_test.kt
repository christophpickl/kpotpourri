package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteBytesBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteStringBody
import com.github.christophpickl.kpotpourri.http4k.GetRequestOpts
import com.github.christophpickl.kpotpourri.http4k.PostRequestOpts
import com.github.christophpickl.kpotpourri.http4k.RequestBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.google.common.io.ByteSource
import org.testng.annotations.Test

@Test class RequestBodyTest {

    fun `prepareBodyAndContentType - When prepare non-RequestWithEntityOpts, Then return null`() {
        val request = GetRequestOpts()

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue null
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body disabled, Then return null`() {
        val request = PostRequestOpts(requestBody = RequestBody.None)

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue null
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body string, Then return string and content type`() {
        val stringEntity = "testStringEntity"
        val request = PostRequestOpts(requestBody = StringBody(stringEntity))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody("text/plain", DefiniteStringBody(stringEntity))
    }

    data class Person(val name: String)

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body JSON, Then return string and content type`() {
        val jsonEntity = Person("foobar")
        val request = PostRequestOpts(requestBody = JsonBody(jsonEntity))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody("application/json", DefiniteStringBody("""{"name":"foobar"}"""))
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body bytes, Then return bytes and content type`() {
        val bytes = ByteSource.wrap(byteArrayOf(0, 1, 1, 0))
        val contentType = "application/jar"
        val request = PostRequestOpts(requestBody = BytesBody(contentType, bytes))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody(contentType, DefiniteBytesBody(bytes))
    }

}
