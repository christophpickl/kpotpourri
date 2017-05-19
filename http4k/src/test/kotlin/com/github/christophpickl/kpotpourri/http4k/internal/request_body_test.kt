package com.github.christophpickl.kpotpourri.http4k.internal

import com.github.christophpickl.kpotpourri.http4k.BodyfullRequestOpts
import com.github.christophpickl.kpotpourri.http4k.BodylessRequestOpts
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteBytesBody
import com.github.christophpickl.kpotpourri.http4k.DefiniteRequestBody.DefiniteStringBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody
import com.github.christophpickl.kpotpourri.http4k.RequestBody.*
import com.github.christophpickl.kpotpourri.jackson4k.asString
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class RequestBodyTest {

    companion object {
        private val mapper = buildJackson4kMapper()
    }

    fun `prepareBodyAndContentType - When prepare non-RequestWithEntityOpts, Then return null`() {
        val request = BodylessRequestOpts()

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue null
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body disabled, Then return null`() {
        val request = BodyfullRequestOpts(requestBody = RequestBody.None)

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue null
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body string, Then return string and content type`() {
        val stringEntity = "testStringEntity"
        val request = BodyfullRequestOpts(requestBody = StringBody(stringEntity))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody("text/plain", DefiniteStringBody(stringEntity))
    }

    data class Person(val name: String) {
        fun toJson() = mapper.asString(this)
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body JSON, Then return string and content type`() {
        val jsonEntity = Person("foobar")
        val request = BodyfullRequestOpts(requestBody = JsonBody(jsonEntity))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody("application/json", DefiniteStringBody(jsonEntity.toJson()))
    }

    fun `prepareBodyAndContentType - When prepare sub-RequestWithEntityOpts with body bytes, Then return bytes and content type`() {
        val bytes = byteArrayOf(0, 1, 1, 0)
        val contentType = "application/jar"
        val request = BodyfullRequestOpts(requestBody = BytesBody(bytes, contentType))

        val actual = prepareBodyAndContentType(request)

        actual shouldMatchValue TypeAndBody(contentType, DefiniteBytesBody(bytes))
    }

}
