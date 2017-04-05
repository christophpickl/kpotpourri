package com.github.christophpickl.kpotpourri.common

/**
 * Base class for any exception thrown from within this library.
 */
open class KPotpourriException(message: String, cause: Exception? = null) : RuntimeException(message, cause)
