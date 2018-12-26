package com.github.christophpickl.kpotpourri.common.reflection

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class ReflectorImplTest {

    fun `lookupClass tests`() {
        ReflectorImpl().lookupClass("java.lang.String") shouldMatchValue String::class.java
        ReflectorImpl().lookupClass("not_found") shouldMatchValue null
    }

}

@Test class ReflectionKtTest {
    
    fun `propertiesOfType sunshine`() {
        val subInstance = Sub()
        val superInstance = Super()
        val testee = HasProperties(subInstance = subInstance, superInstance = superInstance)
        
        val strings = propertiesOfType<HasProperties, Super>(testee)
        
        assertThat(strings, containsExactlyInAnyOrder(subInstance as Super, superInstance))
    }
    
}

@Suppress("unused")
internal class HasProperties(
    val ignored: Int = 0,
    val superInstance: Super,
    val subInstance: Sub
)

internal open class Super
internal class Sub : Super()
