package com.github.christophpickl.kpotpourri.common.string

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test
class StringifierTest {

    fun `When stringify empty Then return empty`() {
        assertStringifyEquals(Empty(), "Empty{}")
    }

    fun `When stringify simple property Then return it`() {
        assertStringifyEquals(SingleProperty(), "SingleProperty{someInt=1}")
    }

    fun `When stringify private property Then dont return it`() {
        assertStringifyEquals(PrivateSingleProperty(), "PrivateSingleProperty{}")
    }

    fun `When stringify inheritance Then return it`() {
        assertStringifyEquals(SubType(), "SubType{subInt=1, superInt=1}")
    }

    fun `When stringify with declared type Then return declared properties stringified`() {
        val declared: Any = SingleProperty()
        assertThat(Stringifier.stringify(declared), equalTo("SingleProperty{}"))
    }

    fun `When stringify ignored property Then dont return it`() {
        assertStringifyEquals(IgnoreProperty(), "IgnoreProperty{}")
    }

    fun `When stringify ignored property on interface Then dont return it`() {
        assertStringifyEquals(IgnoreOnInterfaceProperty(), "IgnoreOnInterfaceProperty{}")
    }

    private inline fun <reified T : Any> assertStringifyEquals(any: T, expected: String) {
        assertThat(Stringifier.stringify(any), equalTo(expected))
    }
}

private class Empty

private class SingleProperty {
    var someInt = 1
}

private class PrivateSingleProperty {
    private var someInt = 1
}

private class IgnoreProperty {
    @IgnoreStringified
    var someInt = 1
}

private open class SuperType {
    var superInt = 1
}

private class SubType : SuperType() {
    var subInt = 1
}

interface IgnoreInterface {
    @IgnoreStringified
    var someInt: Int
}

class IgnoreOnInterfaceProperty : IgnoreInterface {
    override var someInt = 1
}
