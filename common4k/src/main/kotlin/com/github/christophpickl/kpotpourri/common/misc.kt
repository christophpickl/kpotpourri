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
