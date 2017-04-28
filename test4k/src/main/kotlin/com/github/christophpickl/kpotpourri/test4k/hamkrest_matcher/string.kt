package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.containsSubstring

/**
 * Hamcrest matcher to check if the actual string contains all of the given (sub)strings.
 */
fun containsSubstrings(vararg substrings: String) =
        allOf(substrings.map(::containsSubstring))
