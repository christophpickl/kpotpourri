package com.github.christophpickl.kpotpourri.test4k


/**
 * Convert any list of objects to a proper collection of arrays to be used for JUnit parameterized tests.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> List<T>.toParamterized(): Collection<Array<T>> =
        map { arrayOf(it) }
