package com.github.christophpickl.kpotpourri.release4k


enum class VersionType {
    Snapshot,
    Release
}

sealed class Version(open val type: VersionType, private val numbers: List<Int>) {

    val niceString: String by lazy {
        numbers.joinToString(".") + if (type == VersionType.Snapshot) "-SNAPSHOT" else ""
    }

    data class VersionParts1(override val type: VersionType, val version: Int) :
            Version(type, listOf(version)) {
        override fun toVersion1() = this

        fun increment1() = VersionParts1(type, version + 1)
    }

    data class VersionParts2(override val type: VersionType, val version1: Int, val version2: Int) :
            Version(type, listOf(version1, version2)) {
        override fun toVersion2() = this

        fun increment1() = VersionParts2(type, version1 + 1, version2)
        fun increment2() = VersionParts2(type, version1, version2 + 1)
    }

    data class VersionParts3(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int) :
            Version(type, listOf(version1, version2, version3)) {
        override fun toVersion3() = this

        fun increment1() = VersionParts3(type, version1 + 1, version2, version3)
        fun increment2() = VersionParts3(type, version1, version2 + 1, version3)
        fun increment3() = VersionParts3(type, version1, version2, version3 + 1)

    }

    data class VersionParts4(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int, val version4: Int) :
            Version(type, listOf(version1, version2, version3, version4)) {
        override fun toVersion4() = this

        fun increment1() = VersionParts4(type, version1 + 1, version2, version3, version4)
        fun increment2() = VersionParts4(type, version1, version2 + 1, version3, version4)
        fun increment3() = VersionParts4(type, version1, version2, version3 + 1, version4)
        fun increment4() = VersionParts4(type, version1, version2, version3, version4 + 1)

    }

    open fun toVersion1(): VersionParts1 {
        throw Release4kException("Version '$niceString' must contain of 1 part.")
    }

    open fun toVersion2(): VersionParts2 {
        throw Release4kException("Version '$niceString' must contain of 2 parts.")
    }

    open fun toVersion3(): VersionParts3 {
        throw Release4kException("Version '$niceString' must contain of 3 parts.")
    }

    open fun toVersion4(): VersionParts4 {
        throw Release4kException("Version '$niceString' must contain of 4 parts.")
    }

}

