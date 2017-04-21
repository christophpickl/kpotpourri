package com.github.christophpickl.kpotpourri.test4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldBeTrue
import org.testng.annotations.Test

@Test class MiscTest {

    fun `exception contains message`() {
        Exception("a b c d").messageContains("a","b", "c", "d").shouldBeTrue()
    }

}
