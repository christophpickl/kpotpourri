package com.github.christophpickl.kpotpourri.common.logging

import mu.KotlinLogging

/*

// first we did this:
val slf4jLog = LoggerFactory.getLogger(javaClass)

// then kotlin came:
val kotlinLog = KotlinLogging.logger { }

// finally with common4k we do this now:
val log = LOG {}

*/


/**
 * Create Slf4j logger with automatic set logger name.
 *
 * Usage: val log = LOG {}
 */
fun LOG(func: () -> Unit) = KotlinLogging.logger(func)
