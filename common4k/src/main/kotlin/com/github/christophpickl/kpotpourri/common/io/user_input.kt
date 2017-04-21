package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.DefaultBehaviour.*

private val INPUT_SIGN = ">>"

interface ToPrintStringable {
    fun toPrintString(): String
}

sealed class DefaultBehaviour<T> {
    class Disabled<T> : DefaultBehaviour<T>()
    class SelectFirst<T> : DefaultBehaviour<T>()
    data class DefaultValue<T>(val value: T) : DefaultBehaviour<T>()
}

fun <T : ToPrintStringable> readOptions(prompt: String, options: List<T>, defaultOption: DefaultBehaviour<T> = Disabled()): T {
    if (options.map { it.toPrintString() }.distinct().size != options.size) throw KPotpourriException("Options contain duplicate entries!")
    val optionsByPrintString = options.associateBy { it.toPrintString() }
    val stringDefaultOption = when (defaultOption) {
        is SelectFirst<T> -> SelectFirst<String>()
        is Disabled<T> -> Disabled<String>()
        is DefaultValue<T> -> DefaultValue(defaultOption.value.toPrintString())
    }
    val input = readOptions(prompt, optionsByPrintString.keys.toList(), stringDefaultOption)
    return optionsByPrintString[input]!!

}

fun readOptions(prompt: String, options: List<String>, defaultOption: DefaultBehaviour<String> = Disabled()): String {
    if (options.isEmpty()) {
        throw KPotpourriException("Must provide at least one option!")
    }
    if (defaultOption is DefaultValue<String> && !options.contains(defaultOption.value)) {
        throw KPotpourriException("Default option '$defaultOption' must be within: $options")
    }

    println(prompt)
    options.forEachIndexed { i, opt ->
        val defaultSuffix = when (defaultOption) {
            is Disabled<*> -> ""
            is SelectFirst<*> -> if (i == 0) " (default)" else ""
            is DefaultValue<String> -> if (defaultOption.value == opt) " (default)" else ""
        }
        println("[${i + 1}] $opt$defaultSuffix")
    }
    do {
        print("$INPUT_SIGN ")
        val rawInput = readLine()!!.trim()
        if (rawInput == "") {
            if (defaultOption is DefaultValue<String>) {
                return defaultOption.value
            } else if (defaultOption is SelectFirst<String>) {
                return options[0]
            } else { // Disabled
                continue
            }
        }

        val intInput = rawInput.toIntOrNull()
        if (intInput == null) {
            println("Invalid input: '$rawInput'")
            continue
        }
        if (intInput < 1 || intInput > options.size) {
            println("Invalid index: $intInput (must be between 1-${options.size})")
            continue
        }
        return options[intInput - 1]
    } while (true)
}

fun readConfirmation(prompt: String, defaultConfirm: Boolean? = null): Boolean {
    println(prompt)
    val preprompt = when (defaultConfirm) {
        true -> "Y/n"
        false -> "y/N"
        else -> "y/n"
    }
    do {
        print("[$preprompt] $INPUT_SIGN ")
        val input = readLine()!!.trim()
        if (input.toLowerCase() == "y") {
            return true
        }
        if (input.toLowerCase() == "n") {
            return false
        }
        if (defaultConfirm != null && input == "") {
            return defaultConfirm
        }
        if (input != "") {
            val defaultExplanation = when (defaultConfirm) {
                true -> " (or hit Enter for 'y')"
                false -> " (or hit Enter for 'n')"
                else -> ""
            }
            println("Invalid input '$input'. Please enter either 'y' or 'n'$defaultExplanation.")
        }
    } while (true)
}
