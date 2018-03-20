package com.github.christophpickl.kpotpourri.common.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAccessor


/**
 * Utility conversion.
 */
fun TemporalAccessor.toLocalDate() = LocalDate.from(this)!!

/**
 * Utility conversion.
 */
fun TemporalAccessor.toLocalDateTime() = LocalDateTime.from(this)!!
