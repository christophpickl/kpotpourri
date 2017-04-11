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
            Version(type, listOf(version))

    data class VersionParts2(override val type: VersionType, val version1: Int, val version2: Int) :
            Version(type, listOf(version1, version2))

    data class VersionParts3(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int) :
            Version(type, listOf(version1, version2, version3))

    data class VersionParts4(override val type: VersionType, val version1: Int, val version2: Int, val version3: Int, val version4: Int) :
            Version(type, listOf(version1, version2, version3, version4))

}

