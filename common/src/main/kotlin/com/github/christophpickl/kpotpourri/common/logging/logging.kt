package com.github.christophpickl.kpotpourri.common.logging

import mu.KotlinLogging

/**
 * Create Slf4j logger with automatic set logger name.
 *
 * Usage: val log = LOG {}
 */
fun LOG(func: () -> Unit) = KotlinLogging.logger(func)
