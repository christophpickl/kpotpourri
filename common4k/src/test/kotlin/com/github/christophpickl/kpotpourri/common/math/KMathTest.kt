package com.github.christophpickl.kpotpourri.common.math

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test
class KMathTest {
    
    fun `min min`() {
        assertThat(KMath.min(1, 2), equalTo(1))
        assertThat(KMath.min(4, 3), equalTo(3))
        assertThat(KMath.min(4, 3, 2), equalTo(2))
        assertThat(KMath.min(4, 3, 2, 1), equalTo(1))

        assertThat(KMath.minButNotNegative(-1, -2), equalTo(0))
    }
    
}
