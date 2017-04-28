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

// Nothing is final, therefor cant add as <A : Nothing>
// MINOR pseudo-test me
val Nothing.enforceAllBranchesCovered2: Unit get() = Unit
// MINOR maybe can use Any?
val <A> A.enforceAllBranchesCovered: Unit get() = Unit

@Suppress("unused")
fun sleepRand() = Thread.sleep((Math.random() * 500.0).toLong() + 500)
