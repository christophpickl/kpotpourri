package com.github.christophpickl.kpotpourri.markdown4k

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test
import java.io.File

@Test class Markdown4kTest {

    // TODO move to common4k along with the scanForFilesRecursively
    fun `scanForMdFiles - dir1`() {
        assertThat(scanForFilesRecursively(File("src/test/resources/dir1"), "md").map { it.name },
                containsExactlyInAnyOrder("md1.md", "sub1.md"))
    }


}
