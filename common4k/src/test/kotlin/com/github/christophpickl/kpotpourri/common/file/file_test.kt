package com.github.christophpickl.kpotpourri.common.file

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test class FileExtensionsTest {

    private val resources = "src/test/resources"

    @DataProvider
    fun provideFileNames(): Array<Array<out Any?>> = arrayOf(
            arrayOf("foo.bar", "foo", "bar"),
            arrayOf("foobar", "foobar", null),
            arrayOf("foo.bar.baz", "foo.bar", "baz"),
            arrayOf("foo.", "foo.", null),
            arrayOf("foo.bar.", "foo.", "bar."),
            arrayOf(".foo.bar", ".foo", "bar"),
            arrayOf(".foo", ".foo", null)
    )

    @Test(dataProvider = "provideFileNames")
    fun `FileName by`(fullname: String, name: String, suffix: String?) {
        FileName.by(fullname) shouldMatchValue FileName(name, suffix)
    }

    fun `tempFile - sunshine`() {
        val file = tempFile("foo")

        file.exists() shouldMatchValue true
        file.isFile shouldMatchValue true
    }

    fun `verifyExists - Given existing file, Should not throw`() {
        File(".").verifyExists()
    }

    fun `verifyExists - Given non-existing file, Should throw`() {
        assertThrown<KPotpourriException> {
            File("./this/is/not/existing").verifyExists()
        }
    }

    fun `verifyIsFile - Given existing file, Should not throw`() {
        File.createTempFile("existing", ".txt").verifyIsFile()
    }

    fun `verifyIsFile - Given non existing file, Should throw`() {
        val file = File("not_existing.txt")
        assertThrown<KPotpourriException>({ it.message!!.contains(file.canonicalPath) }) {
            file.verifyIsFile()
        }
    }

    fun `verifyExistsAndIsFile - Given existing file, Should not throw`() {
        File.createTempFile("existing", ".txt").verifyExistsAndIsFile()
    }

    fun `verifyExistsAndIsFile - Given existing directory, Should throw`() {
        val file = File(".")
        assertThrown<KPotpourriException>({ it.message!!.contains(file.canonicalPath) }) {
            file.verifyExistsAndIsFile()
        }
    }

    fun `verifyExistsAndIsFile - Given non existing file, Should throw`() {
        val file = File("not_existing.txt")
        assertThrown<KPotpourriException>({ it.message!!.contains(file.canonicalPath) }) {
            file.verifyExistsAndIsFile()
        }
    }

    fun `move - sunshine`() {
        val original = File("$resources/playground/original.txt")
        val target = File("$resources/playground/moved.txt")
        original.touch()
        original.move(target)

        assertThat(original.exists(), equalTo(false))
        assertThat(target.exists(), equalTo(true))

        // cleanup ;)
        target.delete()
    }

    fun `nameStartingFrom sunshine`() {
        assertThat(File("/top/sub/file.txt").nameStartingFrom(File("/top")),
                equalTo("/sub/file.txt"))
    }

    fun `scanForFilesRecursively - Given non existing folder, Should throw`() {
        val file = File("/not/existing")
        assertThrown<KPotpourriException>({ it.message!!.contains(file.canonicalPath) }) {
            file.scanForFilesRecursively("ignore")
        }
    }

    fun `scanForFilesRecursively - Given dir1 and search for txt, Should return file1`() {
        assertScan(folder = "dir1", suffix = "txt", expected = "file1.txt")
    }

    fun `scanForFilesRecursively - Given dir1 and search for TXT, Should return file1`() {
        assertScan(folder = "dir1", suffix = "TXT", expected = "file1.txt")
    }

    fun `scanForFilesRecursively - Given dir2 and search for txt, Should return file1`() {
        assertScan(folder = "dir2", suffix = "txt", expected = "file1.txt")
    }

    fun `scanForFilesRecursively - Given dir3 and search for txt with ignoring subdir, Should return file1`() {
        assertScan(folder = "dir3", suffix = "txt", ignoreFolders = listOf("subdir"), expected = "file1.txt")
    }

    private fun scan(folder: String, suffix: String, ignoreFolders: List<String>) =
            File("./src/test/resources/$folder").scanForFilesRecursively(suffix, ignoreFolders).map { it.name }

    private fun assertScan(folder: String, ignoreFolders: List<String> = emptyList(), suffix: String, vararg expected: String) {
        assertThat(scan(folder, suffix, ignoreFolders), containsExactlyInAnyOrder(*expected))
    }

}
