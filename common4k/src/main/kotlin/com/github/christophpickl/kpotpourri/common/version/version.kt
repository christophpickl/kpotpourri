package com.github.christophpickl.kpotpourri.common.version

interface Version<V : Version<V>> : Comparable<V> {
    fun readOther(input: String) : V?
}

data class Version1(
    val part1: Int
) : Version<Version1> {
    
    companion object : VersionParser<Version1>(1) {
        override fun toVersion(numbers: List<Int>) =
            Version1(numbers[0])
    }
    
    fun incrementPart1() = copy(part1 = part1 + 1)

    override fun readOther(input: String) = parse(input)
    
    override fun compareTo(other: Version1): Int =
        part1.compareTo(other.part1)
    
    override fun toString() = "$part1"
}

data class Version2(
    val part1: Int,
    val part2: Int
) : Version<Version2> {
    
    companion object : VersionParser<Version2>(2) {
        override fun toVersion(numbers: List<Int>) =
            Version2(numbers[0], numbers[1])
    }
    
    fun incrementPart1() = copy(part1 = part1 + 1)
    fun incrementPart2() = copy(part2 = part2 + 1)

    override fun readOther(input: String) = parse(input)
    
    override fun compareTo(other: Version2): Int {
        val compare1 = part1.compareTo(other.part1)
        if (compare1 != 0) return compare1
        return part2.compareTo(other.part2)
    }
    
    override fun toString() = "$part1.$part2"
}

data class Version3(
    val part1: Int,
    val part2: Int,
    val part3: Int
) : Version<Version3> {
    
    companion object : VersionParser<Version3>(3) {
        override fun toVersion(numbers: List<Int>) =
            Version3(numbers[0], numbers[1], numbers[2])
    }
    
    fun incrementPart1() = copy(part1 = part1 + 1)
    fun incrementPart2() = copy(part2 = part2 + 1)
    fun incrementPart3() = copy(part3 = part3 + 1)

    override fun readOther(input: String) = parse(input)
    
    override fun compareTo(other: Version3): Int {
        val compare1 = part1.compareTo(other.part1)
        if (compare1 != 0) return compare1
        val compare2 = part2.compareTo(other.part2)
        if (compare2 != 0) return compare2
        return part3.compareTo(other.part3)
    }
    
    override fun toString() = "$part1.$part2.$part3"
}

data class Version4(
    val part1: Int,
    val part2: Int,
    val part3: Int,
    val part4: Int
) : Version<Version4> {
    
    companion object : VersionParser<Version4>(4) {
        override fun toVersion(numbers: List<Int>) =
            Version4(numbers[0], numbers[1], numbers[2], numbers[3])
    }
    
    fun incrementPart1() = copy(part1 = part1 + 1)
    fun incrementPart2() = copy(part2 = part2 + 1)
    fun incrementPart3() = copy(part3 = part3 + 1)
    fun incrementPart4() = copy(part4 = part4 + 1)

    override fun readOther(input: String) = parse(input)
    
    override fun compareTo(other: Version4): Int {
        val compare1 = part1.compareTo(other.part1)
        if (compare1 != 0) return compare1
        val compare2 = part2.compareTo(other.part2)
        if (compare2 != 0) return compare2
        val compare3 = part3.compareTo(other.part3)
        if (compare3 != 0) return compare3
        return part4.compareTo(other.part4)
    }
    
    override fun toString() = "$part1.$part2.$part3.$part4"
}

abstract class VersionParser<V>(
    partsCount: Int
) {
    private val regex = Regex(
        "^" + 1.rangeTo(partsCount).joinToString("""\.""") { """(\d+)""" } + "$"
    )

    fun parse(input: String): V? {
        val match = regex.matchEntire(input) ?: return null
        val numbers = match.groupValues.drop(1).map { it.toInt() }
        return toVersion(numbers)
    }

    abstract fun toVersion(numbers: List<Int>): V

}
