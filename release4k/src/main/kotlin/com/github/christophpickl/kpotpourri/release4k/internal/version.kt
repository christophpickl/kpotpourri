package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.release4k.Release4kException
import com.github.christophpickl.kpotpourri.release4k.Version
import com.github.christophpickl.kpotpourri.release4k.VersionType
import com.google.common.annotations.VisibleForTesting


internal object VersionParser {

//    fun read() {
//        try to parse = readLine()
//    }

    fun parse(inputString: String): Version {
        val maybeInts = inputString.split(".").map { it.trim().toIntOrNull() }
        val onlyInts = maybeInts.filterNotNull()
        if (maybeInts.size != onlyInts.size) {
            throw VersionParseException(inputString)
        }
        return buildVersion(onlyInts) ?: throw VersionParseException(inputString)
    }

    @VisibleForTesting internal fun buildVersion(numbers: List<Int>): Version? {
        return when (numbers.size) {
            1 -> Version.VersionParts1(VersionType.Release, numbers[0])
            2 -> Version.VersionParts2(VersionType.Release, numbers[0], numbers[1])
            3 -> Version.VersionParts3(VersionType.Release, numbers[0], numbers[1], numbers[2])
            4 -> Version.VersionParts4(VersionType.Release, numbers[0], numbers[1], numbers[2], numbers[3])
            else -> null
        }
    }

}

internal class VersionParseException(inputString: String) : Release4kException("Invalid version string: '$inputString'")
