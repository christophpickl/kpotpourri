package com.github.christophpickl.kpotpourri.http4k

import com.github.christophpickl.kpotpourri.http4k.non_test.testDummy
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class Response4kTest {

    fun `readJson sunshine`() {
        Response4k.testDummy.copy(bodyAsString = "{\"number\":42}").readJson(Dto::class) shouldMatchValue Dto(42)
    }

    private data class Dto(val number: Int)

}
