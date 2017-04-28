package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import org.testng.annotations.Test


@Test class ShouldTest {

    fun `shouldMatchValue, Given equal objects, Should match`() {
        "x" shouldMatchValue "x"
    }

    fun `shouldMatchValue, Given non-equal objects, Should fail`() {
        assertThrown<AssertionError> {
            "x" shouldMatchValue "y"
        }
    }

    fun `shouldNotMatchValue, Given non-equal objects, Should match`() {
        "x" shouldNotMatchValue "y"
    }

    fun `shouldNotMatchValue, Given equal objects, Should fail`() {
        assertThrown<AssertionError> {
            "x" shouldNotMatchValue "x"
        }
    }

    fun `shouldBeTrue, Given true, Should not throw`() {
        true.shouldBeTrue()
    }

    fun `shouldBeTrue, Given false, Should throw`() {
        assertThrown<AssertionError> {
            false.shouldBeTrue()
        }
    }

    fun `shouldBeFalse, Given false, Should not throw`() {
        false.shouldBeFalse()
    }

    fun `shouldBeFalse, Given true, Should throw`() {
        assertThrown<AssertionError> {
            true.shouldBeFalse()
        }
    }

}
