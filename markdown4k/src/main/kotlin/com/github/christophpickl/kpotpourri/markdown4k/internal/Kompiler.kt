package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.markdown4k.KompilationResult
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

internal object Kompiler {

    private val log = LOG {}

    internal fun kompile(code: String): InternalKompilationResult {
        val engine = ScriptEngineManager().getEngineByExtension("kts") ?: throw Exception("No script engine found to kompile Kotlin code.")
        log.trace { "Compiling Kotlin code:\n>>>\n$code\n<<<" }
        try {
            (engine as Compilable).compile(code) // no need to eval, just compile ;)
            return InternalKompilationResult.Success()
        } catch(e: ScriptException) {
            return InternalKompilationResult.Failure(e)
        }
    }

}

/**
 * Introduce custom compilation result which does not include the `Ignored` state.
 */
internal sealed class InternalKompilationResult(private val publicResult: KompilationResult) {
    internal class Success : InternalKompilationResult(KompilationResult.Success())
    internal class Failure(exception: ScriptException) : InternalKompilationResult(KompilationResult.Failure(exception))

    internal fun toKompilationResult() = publicResult
}
