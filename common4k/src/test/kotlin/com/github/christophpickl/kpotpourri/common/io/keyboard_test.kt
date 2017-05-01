package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.ReadOptionsDefaultBehaviour.*
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class KeyboardTest {

    //<editor-fold desc="readConfirmation">

    private val PROMPT = "testPrompt"
    private val ANY_OPTION = "y"

    fun `readConfirmation - Given no default confirm, When enter y, Then confirmed true`() {
        assertReadConfirmation(
                input = "y\n",
                defaultConfirm = null,
                expectedOutput = "$PROMPT\n[y/n] >> ",
                expectedResult = true
        )
    }


    fun `readConfirmation - Given no default confirm, When enter n, Then confirmed false`() {
        assertReadConfirmation(
                input = "n\n",
                defaultConfirm = null,
                expectedOutput = "$PROMPT\n[y/n] >> ",
                expectedResult = false
        )
    }

    fun `readConfirmation - Given no default confirm, When enter invalid, Then invalid input is printed`() {
        val actualOut = Io.readStdoutAndWriteStdin("invalid\n$ANY_OPTION\n") {
            Keyboard.readConfirmation(PROMPT, defaultConfirm = null)
        }
        assertThat(actualOut, containsSubstrings("invalid"))
    }

    fun `readConfirmation - Given default true confirm, When hit enter, Then hint is printed`() {
        val actualOut = Io.readStdoutAndWriteStdin("invalid\n$ANY_OPTION\n") {
            Keyboard.readConfirmation(PROMPT, defaultConfirm = true)
        }
        assertThat(actualOut, containsSubstrings("(or hit Enter for 'y')"))
    }

    fun `readConfirmation - Given default false confirm, When hit enter, Then hint is printed`() {
        val actualOut = Io.readStdoutAndWriteStdin("invalid\ny\n") {
            Keyboard.readConfirmation(PROMPT, defaultConfirm = false)
        }
        assertThat(actualOut, containsSubstrings("(or hit Enter for 'n')"))
    }

    fun `readConfirmation - Given default true confirm, When hit enter, Then confirmed true`() {
        assertReadConfirmation(
                input = "\n",
                defaultConfirm = true,
                expectedOutput = "$PROMPT\n[Y/n] >> ",
                expectedResult = true
        )
    }

    fun `readConfirmation - Given default false confirm, When hit enter, Then confirmed false`() {
        assertReadConfirmation(
                input = "\n",
                defaultConfirm = false,
                expectedOutput = "$PROMPT\n[y/N] >> ",
                expectedResult = false
        )
    }

    private fun assertReadConfirmation(input: String, defaultConfirm: Boolean?, expectedOutput: String, expectedResult: Boolean) {
        var actualConfirmation: Boolean? = null

        val actualOut = Io.readStdoutAndWriteStdin(input) {
            actualConfirmation = Keyboard.readConfirmation(PROMPT, defaultConfirm = defaultConfirm)
        }
        assertThat(actualOut, equalTo(expectedOutput))
        assertThat(actualConfirmation, equalTo(expectedResult))
    }

    //</editor-fold>

    //<editor-fold desc="readOptions">

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

    //</editor-fold>

}
