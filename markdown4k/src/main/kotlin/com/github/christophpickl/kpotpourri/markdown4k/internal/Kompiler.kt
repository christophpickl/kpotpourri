package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.markdown4k.KompilationResult
import mu.KotlinLogging.logger
import javax.script.Compilable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * Start a script engine and compile that thing.
 */
internal object Kompiler {

    private val log = logger {}
    private const val kotlinEngineExtension = "kts"

    internal fun kompile(code: String): InternalKompilationResult {
        log.trace { "Compiling Kotlin code:\n>>>\n$code\n<<<" }
        return try {
            val compiler = lookupKotlinCompiler()
            compiler.compile(code) // no need to eval, just compile
            InternalKompilationResult.Success()
        } catch (e: ScriptException) {
            InternalKompilationResult.Failure(e)
        }
    }

    private fun lookupKotlinCompiler(): Compilable =
            ScriptEngineManager().getEngineByExtension(kotlinEngineExtension) as? Compilable
                    ?: throw KPotpourriException("No script engine found to kompile Kotlin code.")

}

/**
 * Introduce custom compilation result which does not include the `Ignored` state.
 */
internal sealed class InternalKompilationResult(val result: KompilationResult) {

    internal class Success : InternalKompilationResult(KompilationResult.Success)

    internal class Failure(exception: ScriptException) : InternalKompilationResult(KompilationResult.Failure(exception))

}
