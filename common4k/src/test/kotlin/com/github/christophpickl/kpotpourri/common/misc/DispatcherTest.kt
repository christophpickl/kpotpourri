package com.github.christophpickl.kpotpourri.common.misc

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class DispatcherTest {

    private lateinit var dispatcher: Dispatcher<TestListener>
    private lateinit var listener: TestListener
    
    @BeforeMethod
    fun `init state`() {
        dispatcher = Dispatcher()
        listener = mock()
    }

    fun `Given added listener When dispatch Then executed`() {
        dispatcher.add(listener)

        dispatcher.dispatch { executeDispatch() }

        verify(listener, times(1)).executeDispatch()
    }

    fun `Given added listener When remove Then not executed on dispatch`() {
        dispatcher.add(listener)
        dispatcher.remove(listener)

        dispatcher.dispatch { executeDispatch() }

        verify(listener, times(0)).executeDispatch()
    }

    fun `Given added listener When add again Then throw`() {
        dispatcher.add(listener)
        
        assertThrown<IllegalArgumentException> {
            dispatcher.add(listener)
        }
    }

    fun `When remove non-existing listener Then throw`() {
        assertThrown<IllegalArgumentException> {
            dispatcher.remove(listener)
        }
    }

}

private interface TestListener : DispatcherListener {
    fun executeDispatch()
}
