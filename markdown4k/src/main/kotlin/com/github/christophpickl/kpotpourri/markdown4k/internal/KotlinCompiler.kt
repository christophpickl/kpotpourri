package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

internal object KotlinCompiler {

    private val log = LOG {}

    /**
     * @throws ScriptException if code is not compileable
     */
    internal fun compile(code: String) {
        val engine = ScriptEngineManager().getEngineByExtension("kts") ?: throw Exception("No script engine found to compile Kotlin code.")
        log.trace { "Compiling Kotlin code:\n>>>\n$code\n<<<" }
        (engine as Compilable).compile(code) // no need to eval, just compile ;)
    }

}
