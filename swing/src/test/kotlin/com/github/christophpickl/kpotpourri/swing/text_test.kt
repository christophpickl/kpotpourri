package com.github.christophpickl.kpotpourri.swing

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.testng.annotations.Test
import java.awt.Font
import javax.swing.JTextField

@Test class text_test {

    fun `bold should set font style to BOLD`() {
        val text = JTextField()
        text.font = text.font.deriveFont(Font.PLAIN)

        text.bold()

        text.font.style shouldMatch equalTo(Font.BOLD)
    }

}
