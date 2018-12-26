package com.github.christophpickl.kpotpourri.common

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher

fun isBetween(from: Int, to: Int): Matcher<Int?> = object : Matcher<Int?> {
    override val description = "Expected to be between $from and $to."

    override fun invoke(actual: Int?): MatchResult {
        if (actual == null) {
            return MatchResult.Mismatch("Is null, but shouldnt be.")
        }
        if (actual in from..to) {
            return MatchResult.Match
        }
        return MatchResult.Mismatch("Actual $actual not within range $from to $to.")
    }

}
