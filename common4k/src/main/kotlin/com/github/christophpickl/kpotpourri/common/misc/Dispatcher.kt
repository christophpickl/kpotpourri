package com.github.christophpickl.kpotpourri.common.misc

/**
 * Marker interface only.
 */
interface DispatcherListener

/**
 * Provides simple register and dispatch possibility.
 */
class Dispatcher<L : DispatcherListener> {

    private val listeners = LinkedHashSet<L>()

    fun add(listener: L) {
        require(!listeners.contains(listener)) { "Listener was already added: $listener" }
        listeners += listener
    }

    fun remove(listener: L) {
        require(listeners.contains(listener)) { "Listener was not added yet: $listener" }
        listeners.remove(listener)
    }

    fun dispatch(action: L.() -> Unit) {
        listeners.forEach {
            action(it)
        }
    }
}
