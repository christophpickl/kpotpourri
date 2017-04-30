package com.github.christophpickl.kpotpourri.common

/**
 * Used to configure compiler plugin to create a no-arg ctor for classes/annotations with this annotation.
 *
 * In your build.gradle file add the following:
 *
 *   apply plugin: "kotlin-noarg"
 *   noArg {
 *     annotation("com.github.christophpickl.kpotpourri.common.KotlinNoArg")
 *   }
 */
annotation class KotlinNoArg

/**
 * Kotlin workaround to enforce the compiler to check all branches even when not using the when as a statement rather an expression.
 */
@Suppress("unused")
val Any.enforceAllBranchesCovered: Unit get() = Unit

/**
 * Let the thread sleep some random time between 0.5 and 1.0 seconds.
 */
@Suppress("unused")
fun sleepRand() = Thread.sleep((Math.random() * 500.0).toLong() + 500)
