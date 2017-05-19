@file:Suppress("KDocMissingDocumentation")

package com.github.christophpickl.kpotpourri.release4k

import com.github.christophpickl.kpotpourri.common.io.Keyboard

/**
 * Distinction between snapshots and releases.
 */
enum class VersionType {

    /** Identifies a regular Maven snapshot version. */
    Snapshot,

    /** Indentifies a stable, released version. */
    Release // not yet used...

}

private val pattern2 = """^(\d+)\.(\d+)$""".toRegex()
private val pattern3 = """^(\d+)\.(\d+)\.(\d+)$""".toRegex()
private val pattern4 = """^(\d+)\.(\d+)\.(\d+)\.(\d+)$""".toRegex()

/**
 * Parse an input like "42" or return null.
 */
fun parseVersion1(input: String): Version.VersionParts1? {
    val entered = input.toIntOrNull() ?: return null
    return Version.VersionParts1(VersionType.Release, entered)
}

/**
 * Parse an input like "1.2" or return null.
 */
fun parseVersion2(input: String): Version.VersionParts2? {
    val match = pattern2.matchEntire(input) ?: return null
    return Version.VersionParts2(VersionType.Release,
            match.groupValues[1].toInt(),
            match.groupValues[2].toInt())
}

/**
 * Parse an input like "1.2.3" or return null.
 */
fun parseVersion3(input: String): Version.VersionParts3? {
    val match = pattern3.matchEntire(input) ?: return null
    return Version.VersionParts3(VersionType.Release,
            match.groupValues[1].toInt(),
            match.groupValues[2].toInt(),
            match.groupValues[3].toInt())
}

/**
 * Parse an input like "1.2.3.4" or return null.
 */
fun parseVersion4(input: String): Version.VersionParts4? {
    val match = pattern4.matchEntire(input) ?: return null
    return Version.VersionParts4(VersionType.Release,
            match.groupValues[1].toInt(),
            match.groupValues[2].toInt(),
            match.groupValues[3].toInt(),
            match.groupValues[4].toInt())
}

private val DEFAULT_PROMPT = "Enter version"

/**
 * Core type representing different version structures.
 */
sealed class Version(open val type: VersionType, private val numbers: List<Int>) {

    companion object {

        private fun <T : Version> _readFromStdin(parser: (String) -> T?, prompt: String, defaultVersion: T? = null): T {
            val defaultPrompt = if (defaultVersion != null) " [${defaultVersion.niceString}]" else ""
            while (true) {
                print("$prompt$defaultPrompt: ")
                val input = Keyboard.readLine().trim()
                if (input.isEmpty()) {
                    if (defaultVersion != null) {
                        return defaultVersion
                    }
                    continue
                }
                val found = parser(input)
                if (found == null) {
                    println("Invalid input '$input'!")
                } else {
                    return found
                }
            }
        }

    }

    /**
     * Format as readable string, like "1.2.3-SNAPSHOT" or "1.2".
     */
    val niceString: String by lazy {
        numbers.joinToString(".") + if (type == VersionType.Snapshot) "-SNAPSHOT" else ""
    }

    /**
     * Single part version declaration like "1".
     */
    data class VersionParts1(override val type: VersionType, val version: Int) :
            Version(type, listOf(version)) {

        companion object {
            fun readVersion1FromStdin(prompt: String = DEFAULT_PROMPT, defaultVersion: VersionParts1? = null): VersionParts1 {
                return _readFromStdin(::parseVersion1, prompt, defaultVersion)
            }
        }

        override fun toVersion1() = this

        fun increment1() = VersionParts1(type, version + 1)
    }

    /**
     * Two part version declaration like "1.2" (major, minor).
     */
    data class VersionParts2(override val type: VersionType, val version1: Int, val version2: Int) :
            Version(type, listOf(version1, version2)) {

        companion object {
            fun readVersion2FromStdin(prompt: String = DEFAULT_PROMPT, defaultVersion: VersionParts2? = null): VersionParts2 {
                return _readFromStdin(::parseVersion2, prompt, defaultVersion)
            }
        }

        override fun toVersion2() = this
        fun increment1() = VersionParts2(type, version1 + 1, version2)
        fun incrementMajor() = increment1()
        fun increment2() = VersionParts2(type, version1, version2 + 1)
        fun incrementMinor() = increment2()
    }

    /**
     * Three part version declaration like "1.2.3" (major, minor, patch).
     */
    data class VersionParts3(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int) :
            Version(type, listOf(version1, version2, version3)) {

        companion object {
            fun readVersion3FromStdin(prompt: String = DEFAULT_PROMPT, defaultVersion: VersionParts3? = null): VersionParts3 {
                return _readFromStdin(::parseVersion3, prompt, defaultVersion)
            }
        }

        override fun toVersion3() = this
        fun increment1() = VersionParts3(type, version1 + 1, version2, version3)
        fun incrementMajor() = increment1()
        fun increment2() = VersionParts3(type, version1, version2 + 1, version3)
        fun incrementMinor() = increment2()
        fun increment3() = VersionParts3(type, version1, version2, version3 + 1)
        fun incrementPatch() = increment3()

    }

    /**
     * Four part version declaration like "1.2.3.4" (major, minor, patch, buidl).
     */
    data class VersionParts4(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int, val version4: Int) :
            Version(type, listOf(version1, version2, version3, version4)) {

        companion object {
            fun readVersion4FromStdin(prompt: String = DEFAULT_PROMPT, defaultVersion: VersionParts4? = null): VersionParts4 {
                return _readFromStdin(::parseVersion4, prompt, defaultVersion)
            }
        }

        override fun toVersion4() = this

        /** Synonym for `incrementMajor`. */
        fun increment1() = VersionParts4(type, version1 + 1, version2, version3, version4)

        /** Synonym for `increment`. */
        fun incrementMajor() = increment1()

        fun increment2() = VersionParts4(type, version1, version2 + 1, version3, version4)
        fun incrementMinor() = increment2()
        fun increment3() = VersionParts4(type, version1, version2, version3 + 1, version4)
        fun incrementPatch() = increment3()
        fun increment4() = VersionParts4(type, version1, version2, version3, version4 + 1)
        fun incrementBuild() = increment4()

    }

    /** Override in order to enable version part conversion. */
    open fun toVersion1(): VersionParts1 {
        throw Release4kException("Version '$niceString' must consist of 1 part.")
    }

    /** Override in order to enable version part conversion. */
    open fun toVersion2(): VersionParts2 {
        throw Release4kException("Version '$niceString' must consist of 2 parts.")
    }

    /** Override in order to enable version part conversion. */
    open fun toVersion3(): VersionParts3 {
        throw Release4kException("Version '$niceString' must consist of 3 parts.")
    }

    /** Override in order to enable version part conversion. */
    open fun toVersion4(): VersionParts4 {
        throw Release4kException("Version '$niceString' must consist of 4 parts.")
    }

}
