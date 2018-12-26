package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.collection.CachedEntity
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test
class CachedEntityTest {
    private val anyValue = "anyValue"

    fun `When get once Then return value`() {
        val cached = CachedEntity {
            anyValue
        }

        val actual = cached.get()

        assertThat(actual, equalTo(anyValue))
    }

    fun `When get twice Then return invoked once`() {
        var invoked = 0
        val cached = CachedEntity {
            invoked++
            anyValue
        }

        cached.get()
        cached.get()

        assertThat(invoked, equalTo(1))
    }

    fun `When get and invalidate and get Then return invoked twice`() {
        var invoked = 0
        val cached = CachedEntity {
            invoked++
            anyValue
        }

        cached.get()
        cached.invalidate()
        cached.get()

        assertThat(invoked, equalTo(2))
    }

}
