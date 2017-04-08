package com.github.christophpickl.kpotpourri.common.testinfra

import org.testng.Assert
import org.testng.SkipException


/**
 * Simplify skiping a TestNG test.
 */
fun skip(message: String) {
    throw SkipException(message)
}
