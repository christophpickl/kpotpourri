package com.github.christophpickl.kpotpourri.common.io

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.io.ReadOptionsDefaultBehaviour.*
import com.github.christophpickl.kpotpourri.common.string.containsAll
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsSubstrings
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test
import java.io.ByteArrayInputStream

@Test class KeyboardTest {

    private val PROMPT = "testPrompt"

    //<editor-fold desc="readLine">

    fun `readLine - sunshine`() {
        var read: String? = null
        Io.writeToStdIn("in\n") {
            read = Keyboard.readLine()
        }
        read shouldMatchValue "in"
    }

    fun `readLine - provoking null`() {
        val old = System.`in`
        try {
            System.setIn(ByteArrayInputStream(byteArrayOf()))
            assertThrown<KPotpourriException>({ it.message!!.contains("returned null") }) {
                Keyboard.readLine()
            }
        } finally {
            System.setIn(old)
        }
    }

    //</editor-fold>

    //<editor-fold desc="readConfirmation">

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

    fun `readOptions - Given default option disabled, When type 1, Then second item is returned`() {
        assertReadOptions(
                options = listOf("a", "b"),
                defaultBehaviour = Disabled(),
                input = "1\n",
                expectedOption = "a"
        )
    }

    fun `readOptions - Given default option disabled, When hit enter, Then simply continue as usual`() {
        assertReadOptions(
                options = listOf("a", "b"),
                defaultBehaviour = Disabled(),
                input = "\n1\n",
                expectedOption = "a"
        )
    }

    fun `readOptions - Given default select first, When hit enter, Then first item is returned`() {
        assertReadOptions(
                options = listOf("a", "b"),
                defaultBehaviour = SelectFirst(),
                input = "\n",
                expectedOption = "a"
        )
    }

    fun `readOptions - Given default value of second option, When hit enter, Then second item is returned`() {
        assertReadOptions(
                options = listOf("a", "b"),
                defaultBehaviour = DefaultValue("b"),
                input = "\n",
                expectedOption = "b"
        )
    }

    fun `readOptions - Given empty options, Then throw`() {
        assertThrown<KPotpourriException> {
            Keyboard.readOptions(PROMPT, emptyList())
        }
    }

    fun `readOptions - Given default option which is not in options list, Then throw`() {
        assertThrown<KPotpourriException>({ it.message!!.containsAll("opt1", "not existing") }) {
            Keyboard.readOptions(PROMPT, listOf("opt1"), defaultBehaviour = DefaultValue("not existing"))
        }
    }

    private fun assertReadOptions(
            defaultBehaviour: ReadOptionsDefaultBehaviour<String>,
            options: List<String>,
            input: String,
            expectedOption: String) {
        var actualInput: String? = null

        Io.readStdoutAndWriteStdin(input) {
            actualInput = Keyboard.readOptions(PROMPT, options, defaultBehaviour = defaultBehaviour)
        }
        assertThat(actualInput, equalTo(expectedOption))
    }

    private val person1 = Person("p1")
    private val person2 = Person("p2")

    fun `readTypedOptions - Given default disabled, When type 2, Then second person is returned`() {
        assertReadTypedOptions(
                defaultBehaviour = Disabled(),
                options = listOf(person1, person2),
                input = "2\n",
                expected = person2
        )
    }

    fun `readTypedOptions - Given default value of first, When hit enter, Then first person is returned`() {
        assertReadTypedOptions(
                defaultBehaviour = SelectFirst(),
                options = listOf(person1, person2),
                input = "\n",
                expected = person1
        )
    }

    fun `readTypedOptions - Given default value of second person, When hit enter, Then second person is returned`() {
        assertReadTypedOptions(
                defaultBehaviour = DefaultValue(person2),
                options = listOf(person1, person2),
                input = "\n",
                expected = person2
        )
    }

    fun `readTypedOptions - output`() {
        val output = Io.readStdoutAndWriteStdin("\n") {
            Keyboard.readTypedOptions(PROMPT, listOf(person1, person2), defaultBehaviour = SelectFirst())
        }

        output shouldMatchValue """$PROMPT
[1] ${person1.toPrintString()} (default)
[2] ${person2.toPrintString()}
>> """
    }

    private fun assertReadTypedOptions(
            defaultBehaviour: ReadOptionsDefaultBehaviour<Person>,
            options: List<Person>,
            input: String,
            expected: Person
    ) {
        var actualInput: Person? = null
        Io.writeToStdIn(input) {
            actualInput = Keyboard.readTypedOptions(PROMPT, options, defaultBehaviour = defaultBehaviour)
        }
        assertThat(actualInput, equalTo(expected))
    }

    private data class Person(val name: String) : ToPrintStringable {
        override fun toPrintString() = name
    }

    //</editor-fold>

}
