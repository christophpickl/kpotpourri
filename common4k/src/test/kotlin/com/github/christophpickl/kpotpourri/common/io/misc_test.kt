package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import org.testng.annotations.Test
import java.io.Closeable

@Test class MiscTest {

    fun `Any println - simple`() {
        Io.readFromStdOut { "a".println() } shouldMatchValue "a\n"
    }

    fun `Any println - null`() {
        Io.readFromStdOut { null.println() } shouldMatchValue "null\n"
    }

    fun `Closeable closeSilently - When thrown, Then nothing thrown`() {
        Closeable { throw Exception() }.closeSilently()
    }

}
