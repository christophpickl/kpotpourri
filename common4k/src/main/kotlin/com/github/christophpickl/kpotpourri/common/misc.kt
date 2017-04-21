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

enum class Two { A, B }
/*
fun whenTwo(two: Two) {
    // no error, just a warning
    when(two) {
        Two.A -> return
    }
    // error but introduces warning about unused :-/
    val unused = when(two) {
        Two.A -> return
    }
    // as nice it can get
    when(two) {
        Two.A -> return
    }.safe
}
fun whenTwo2(two: Two) = when (two) {
    Two.A -> Unit
// compile error
}
 */
fun foo() {
    "".enforceAllBranchesCovered
    Unit.enforceAllBranchesCovered
//    Nothing.enforceAllBranchesCovered // ... nope
//    val two = Two.A
//    val x: Nothing = when(two) {
//        Two.A -> return
//    }
//    when(two) {
//        Two.A -> return
//    }.enforceAllBranchesCovered2
}
// Nothing is final, therefor cant add as <A : Nothing>
val Nothing.enforceAllBranchesCovered2: Unit get() = Unit
val <A> A.enforceAllBranchesCovered: Unit get() = Unit

fun sleepRand() = Thread.sleep((Math.random() * 500.0).toLong() + 500)
