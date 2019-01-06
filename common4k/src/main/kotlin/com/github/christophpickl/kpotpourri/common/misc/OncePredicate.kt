package com.github.christophpickl.kpotpourri.common.misc

/**
 * Evaluates the predicate only once and stores it's state.
 */
open class OncePredicate(
    private val predicate: () -> Boolean
) {

    private var wasEnabled = false

    val enabled get() = wasEnabled
    
    /**
     * Evaluate and store result if true.
     */
    fun checkAndGet(): OnceResult =
        if (wasEnabled) {
            OnceResult.WasAlready
        } else {
            val checked = predicate()
            if (checked) {
                wasEnabled = true
                OnceResult.ChangedToTrue
            } else {
                OnceResult.Unfilfilled
            }
        }

}

/**
 * In order to be more specific rather than true/false.
 */
enum class OnceResult {
    Unfilfilled,
    ChangedToTrue,
    WasAlready
}
