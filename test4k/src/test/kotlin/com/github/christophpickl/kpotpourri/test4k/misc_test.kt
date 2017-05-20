package com.github.christophpickl.kpotpourri.test4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldBeFalse
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldBeTrue
import org.testng.annotations.Test

@Test class MiscTest {

    fun `messageContains - sunshine`() {
        Exception("a b c d").messageContains("a", "b", "c", "d").shouldBeTrue()
    }

    fun `messageContains - fail`() {
        Exception("a").messageContains("b").shouldBeFalse()
    }

    fun `messageContains - Given no message, Returns false`() {
        Exception().messageContains("any").shouldBeFalse()
    }

}
