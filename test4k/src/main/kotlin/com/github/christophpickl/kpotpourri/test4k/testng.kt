package com.github.christophpickl.kpotpourri.test4k

import org.testng.Assert
import org.testng.SkipException


/**
 * Simplify skiping a TestNG test.
 */
fun skip(message: String) {
    throw SkipException(message)
}
