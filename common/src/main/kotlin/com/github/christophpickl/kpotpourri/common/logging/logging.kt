package com.github.christophpickl.kpotpourri.common.logging

import mu.KotlinLogging


// TODO see logback logger infra in (put in own module?):
// com.github.christophpickl.kpotpourri.common.testinfra

fun LOG(func: () -> Unit) = KotlinLogging.logger(func)

fun LOG(clazz: Class<out Any>) = KotlinLogging.logger(clazz.simpleName)
