package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.ReadOptionsDefaultBehaviour.*
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class KeyboardTest {

    private val PROMPT = "testPrompt"
    private val INVALID = "testInvalid"

    fun `readConfirmation - Given no default confirm, When enter y, Then confirmed true`() {
        var actualConfirmation: Boolean? = null

        val actualOut = Io.readStdoutAndWriteStdin("y\n") {
            actualConfirmation = Keyboard.readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, equalTo("$PROMPT\n[y/n] >> "))
        assertThat(actualConfirmation, equalTo(true))
    }

    fun `readConfirmation - Given no default confirm, When enter n, Then confirmed false`() {
        var actualConfirmation: Boolean? = null
        val actualOut = Io.readStdoutAndWriteStdin("n\n") {
            actualConfirmation = Keyboard.readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, equalTo("$PROMPT\n[y/n] >> "))
        assertThat(actualConfirmation, equalTo(false))
    }

    fun `readConfirmation - Given no default confirm, When enter invalid, Then confirmed false`() {
        println("Invalid input 'testinvalid'. Please enter".contains("testInvalid"))

        val actualOut = Io.readStdoutAndWriteStdin("$INVALID\ny\n") {
            Keyboard.readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, containsSubstrings(PROMPT, INVALID))
    }

    fun `readConfirmation - Given default true confirm, When hit enter, Then confirmed true`() {
        var actualConfirmation: Boolean? = null

        Io.hitEnterAndReadStdout {
            actualConfirmation = Keyboard.readConfirmation(PROMPT, defaultConfirm = true)
        }
        assertThat(actualConfirmation, equalTo(true))
    }

    private val firstOption = "a"
    private val secondOption = "b"
    private val optionsString = listOf(firstOption, secondOption)

    fun `readOptions stringed - Given default option disabled, When enter 2, Then second item is returned`() {
        var actualInput: String? = null

        Io.readStdoutAndWriteStdin("2\n") {
            actualInput = Keyboard.readOptions(PROMPT, optionsString, defaultBehaviour = Disabled())
        }
        assertThat(actualInput, equalTo(secondOption))
    }

    fun `readOptions stringed - Given default select first, When hit enter, Then first item is returned`() {
        var actualInput: String? = null
        Io.hitEnterAndReadStdout {
            actualInput = Keyboard.readOptions(PROMPT, optionsString, defaultBehaviour = SelectFirst())
        }
        assertThat(actualInput, equalTo(firstOption))
    }

    fun `readOptions stringed - Given default value of second option, When hit enter, Then second item is returned`() {
        var actualInput: String? = null
        Io.hitEnterAndReadStdout {
            actualInput = Keyboard.readOptions(PROMPT, optionsString, defaultBehaviour = DefaultValue(secondOption))
        }
        assertThat(actualInput, equalTo(secondOption))
    }

    fun `readOptions stringed - Given empty options, Then throw`() {
        assertThrown<KPotpourriException> {
            Keyboard.readOptions(PROMPT, emptyList())
        }
    }

    fun `readOptions stringed - Given default option which is not in options list, Then throw`() {
        assertThrown<KPotpourriException> {
            Keyboard.readOptions(PROMPT, listOf("a"), defaultBehaviour = DefaultValue("not existing"))
        }
    }

    fun `readTypedOptions - Given default value of second item, When hit enter, Then second item is returned`() {
        val person1 = Person("p1")
        val person2 = Person("p2")
        val optionsPerson = listOf(person1, person2)

        var actualInput: Person? = null
        Io.hitEnterAndReadStdout {
            actualInput = Keyboard.readTypedOptions(PROMPT, optionsPerson, defaultBehaviour = DefaultValue(person2))
        }
        assertThat(actualInput, equalTo(person2))
    }

    private data class Person(val name: String) : ToPrintStringable {
        override fun toPrintString() = name
    }

}
