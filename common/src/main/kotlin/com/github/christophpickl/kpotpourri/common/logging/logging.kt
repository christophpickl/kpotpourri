package com.github.christophpickl.kpotpourri.common.logging

import mu.KotlinLogging

fun LOG(func: () -> Unit) = KotlinLogging.logger(func)

fun LOG(clazz: Class<out Any>) = KotlinLogging.logger(clazz.simpleName)
