package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.DefaultBehaviour.*
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class UserInputTest {

    private val PROMPT = "testPrompt"
    private val INVALID = "testInvalid"

    // readConfirmation()
    // =================================================================================================================

    fun `readConfirmation - Given no default confirm, When enter y, Then confirmed true`() {
        var actualConfirmation: Boolean? = null

        val actualOut = readStdoutAndWriteStdin("y\n") {
            actualConfirmation = readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, equalTo("$PROMPT\n[y/n] >> "))
        assertThat(actualConfirmation, equalTo(true))
    }

    fun `readConfirmation - Given no default confirm, When enter n, Then confirmed false`() {
        var actualConfirmation: Boolean? = null
        val actualOut = readStdoutAndWriteStdin("n\n") {
            actualConfirmation = readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, equalTo("$PROMPT\n[y/n] >> "))
        assertThat(actualConfirmation, equalTo(false))
    }

    fun `readConfirmation - Given no default confirm, When enter invalid, Then confirmed false`() {
        println("Invalid input 'testinvalid'. Please enter".contains("testInvalid"))

        val actualOut = readStdoutAndWriteStdin("$INVALID\ny\n") {
            readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, containsSubstrings(PROMPT, INVALID))
    }

    fun `readConfirmation - Given default true confirm, When hit enter, Then confirmed true`() {
        var actualConfirmation: Boolean? = null

        hitEnterAndReadStdout {
            actualConfirmation = readConfirmation(PROMPT, defaultConfirm = true)
        }
        assertThat(actualConfirmation, equalTo(true))
    }

    // readOptions() stringed
    // =================================================================================================================

    private val firstOption = "a"
    private val secondOption = "b"
    private val optionsString = listOf(firstOption, secondOption)

    fun `readOptions stringed - Given default option disabled, When enter 2, Then second item is returned`() {
        var actualInput: String? = null

        readStdoutAndWriteStdin("2\n") {
            actualInput = readOptions(PROMPT, optionsString, defaultOption = Disabled())
        }
        assertThat(actualInput, equalTo(secondOption))
    }

    fun `readOptions stringed - Given default select first, When hit enter, Then first item is returned`() {
        var actualInput: String? = null
        hitEnterAndReadStdout {
            actualInput = readOptions(PROMPT, optionsString, defaultOption = SelectFirst())
        }
        assertThat(actualInput, equalTo(firstOption))
    }

    fun `readOptions stringed - Given default value of second option, When hit enter, Then second item is returned`() {
        var actualInput: String? = null
        hitEnterAndReadStdout {
            actualInput = readOptions(PROMPT, optionsString, defaultOption = DefaultValue(secondOption))
        }
        assertThat(actualInput, equalTo(secondOption))
    }

    fun `readOptions stringed - Given empty options, Then throw`() {
        assertThrown<KPotpourriException> {
            readOptions(PROMPT, emptyList())
        }
    }

    fun `readOptions stringed - Given default option which is not in options list, Then throw`() {
        assertThrown<KPotpourriException> {
            readOptions(PROMPT, listOf("a"), defaultOption = DefaultValue("not existing"))
        }
    }

    // readOptions() typed
    // =================================================================================================================

    private val person1 = Person("p1")
    private val person2 = Person("p2")
    private val optionsPerson = listOf(person1, person2)

    fun `readOptions typed - Given default value of second item, When hit enter, Then second item is returned`() {
        var actualInput: Person? = null
        hitEnterAndReadStdout {
            actualInput = readOptions(PROMPT, optionsPerson, defaultOption = DefaultValue(person2))
        }
        assertThat(actualInput, equalTo(person2))
    }

    private data class Person(val name: String) : ToPrintStringable {
        override fun toPrintString() = name
    }

}
