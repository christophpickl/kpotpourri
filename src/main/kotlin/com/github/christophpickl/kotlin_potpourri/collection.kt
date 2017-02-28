package com.github.christophpickl.kotlin_potpourri


fun List<Any>.toPrettyString() = map { "- ${it.toString()}" }.joinToString("\n")

fun List<Any>.prettyPrint() {
    println(toPrettyString())
}
