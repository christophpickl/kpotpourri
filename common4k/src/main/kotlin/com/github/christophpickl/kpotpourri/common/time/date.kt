package com.github.christophpickl.kpotpourri.common.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalAccessor
import java.util.*

/**
 * Utility conversion.
 */
fun TemporalAccessor.toLocalDate(): LocalDate = LocalDate.from(this)

/**
 * Utility conversion.
 */
fun TemporalAccessor.toLocalDateTime() = LocalDateTime.from(this)!!

fun Date.toLocalDateTime(): LocalDateTime = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDate.isBeforeOrEqual(other: LocalDate) =
    isBefore(other) || isEqual(other)
