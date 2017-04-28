package com.github.christophpickl.kpotpourri.common.enum

// MINOR test me
inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}
