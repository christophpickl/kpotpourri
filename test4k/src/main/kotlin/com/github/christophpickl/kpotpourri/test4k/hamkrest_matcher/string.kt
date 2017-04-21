package com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher

import com.natpryce.hamkrest.containsSubstring

fun containsSubstrings(vararg substrings: String) =
        allOf(substrings.map { containsSubstring(it) })
