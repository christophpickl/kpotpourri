package com.github.christophpickl.kpotpourri.swing

import com.github.christophpickl.kpotpourri.common.testinfra.shouldMatchValue
import com.github.christophpickl.kpotpourri.common.testinfra.shouldNotMatchValue
import org.testng.annotations.Test
import java.awt.Font
import javax.swing.JTextField

@Test class JTextFieldExtensionsTest {

    fun `bold should set font style to BOLD`() {
        val text = JTextField()
        text.font.style shouldNotMatchValue Font.BOLD

        text.bold()

        text.font.style shouldMatchValue Font.BOLD
    }

}
