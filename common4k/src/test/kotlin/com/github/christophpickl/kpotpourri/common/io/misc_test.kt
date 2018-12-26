package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test
import java.io.Closeable

@Test class MiscTest {

    fun `Closeable closeSilently - When thrown, Then nothing thrown`() {
        Closeable { throw Exception() }.closeSilently()
    }

}
