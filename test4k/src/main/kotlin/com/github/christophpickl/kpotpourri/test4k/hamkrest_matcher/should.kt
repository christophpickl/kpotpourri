package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

/**
 * Hamcrest matcher to check for equality made kotlin-ish.
 *
 * Usage: "x" shouldMatchValue "x"
 */
infix fun <T> T.shouldMatchValue(expectedValue: T) {
    assertThat(this, equalTo(expectedValue))
}

/**
 * Hamcrest matcher to check for non-equality made kotlin-ish.
 *
 * Usage: "x" shouldNotMatchValue "y"
 */
infix fun <T> T.shouldNotMatchValue(expectedValue: T) {
    assertThat(this, not(equalTo(expectedValue)))
}


/**
 * Hamcrest shortcut matcher to check if the given Boolean is true.
 */
fun Boolean.shouldBeTrue() {
    this shouldMatchValue true
}

/**
 * Hamcrest shortcut matcher to check if the given Boolean is false.
 */
fun Boolean.shouldBeFalse() {
    this shouldMatchValue false
}
