package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.skip
import org.testng.annotations.Test
import java.io.File

@Test class FileExtensionsTest {

    fun `verifyExists - Given existing file, Should not throw`() {
        File(".").verifyExists()
    }

    fun `verifyExists - Given non-existing file, Should throw`() {
        assertThrown<KPotpourriException> {
            File("./this/is/not/existing").verifyExists()
        }
    }

    fun `move`() {
        skip("how to test File.move()?")
    }
}
