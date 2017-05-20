package com.github.christophpickl.kpotpourri.common.reflection

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test

@Test class ReflectionTest {

    fun `lookupClass`() {
        ReflectorImpl().lookupClass("java.lang.String") shouldMatchValue String::class.java
        ReflectorImpl().lookupClass("not_found") shouldMatchValue null
    }

}
