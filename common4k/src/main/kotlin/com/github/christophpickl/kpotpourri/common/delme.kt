package com.github.christophpickl.kpotpourri.common

import com.github.christophpickl.kpotpourri.common.logging.LOG
import mu.KotlinLogging
import org.slf4j.LoggerFactory

class Logee {
    // first we did this:
    val slf4jLog = LoggerFactory.getLogger(javaClass)

    // then kotlin came:
    val kotlinLog = KotlinLogging.logger { }

    // finally with common4k:
    val log = LOG {}
}
