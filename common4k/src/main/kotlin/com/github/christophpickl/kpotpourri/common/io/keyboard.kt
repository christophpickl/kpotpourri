package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException

object Keyboard {

    private val INPUT_SIGN = ">>"

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

    fun readOptions(prompt: String, options: List<String>, defaultBehaviour: ReadOptionsDefaultBehaviour<String> = ReadOptionsDefaultBehaviour.Disabled()): String {
        if (options.isEmpty()) {
            throw KPotpourriException("Must provide at least one option!")
        }
        if (defaultBehaviour is ReadOptionsDefaultBehaviour.DefaultValue<String> && !options.contains(defaultBehaviour.value)) {
            throw KPotpourriException("Default option '$defaultBehaviour' must be within: $options")
        }

        println(prompt)
        options.forEachIndexed { i, opt ->
            val defaultSuffix = when (defaultBehaviour) {
                is ReadOptionsDefaultBehaviour.Disabled<*> -> ""
                is ReadOptionsDefaultBehaviour.SelectFirst<*> -> if (i == 0) " (default)" else ""
                is ReadOptionsDefaultBehaviour.DefaultValue<String> -> if (defaultBehaviour.value == opt) " (default)" else ""
            }
            println("[${i + 1}] $opt$defaultSuffix")
        }
        do {
            print("$INPUT_SIGN ")
            val rawInput = readLine()!!.trim()
            if (rawInput == "") {
                if (defaultBehaviour is ReadOptionsDefaultBehaviour.DefaultValue<String>) {
                    return defaultBehaviour.value
                } else if (defaultBehaviour is ReadOptionsDefaultBehaviour.SelectFirst<String>) {
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

    fun <T : ToPrintStringable> readTypedOptions(prompt: String, options: List<T>, defaultBehaviour: ReadOptionsDefaultBehaviour<T> = ReadOptionsDefaultBehaviour.Disabled()): T {
        if (options.map { it.toPrintString() }.distinct().size != options.size) throw KPotpourriException("Options contain duplicate entries!")
        val optionsByPrintString = options.associateBy { it.toPrintString() }
        val stringDefaultOption = when (defaultBehaviour) {
            is ReadOptionsDefaultBehaviour.SelectFirst<T> -> ReadOptionsDefaultBehaviour.SelectFirst<String>()
            is ReadOptionsDefaultBehaviour.Disabled<T> -> ReadOptionsDefaultBehaviour.Disabled<String>()
            is ReadOptionsDefaultBehaviour.DefaultValue<T> -> ReadOptionsDefaultBehaviour.DefaultValue(defaultBehaviour.value.toPrintString())
        }
        val input = readOptions(prompt, optionsByPrintString.keys.toList(), stringDefaultOption)
        return optionsByPrintString[input]!!

    }


}

interface ToPrintStringable {
    fun toPrintString(): String
}

sealed class ReadOptionsDefaultBehaviour<T> {
    class Disabled<T> : ReadOptionsDefaultBehaviour<T>()
    class SelectFirst<T> : ReadOptionsDefaultBehaviour<T>()
    data class DefaultValue<T>(val value: T) : ReadOptionsDefaultBehaviour<T>()
}
