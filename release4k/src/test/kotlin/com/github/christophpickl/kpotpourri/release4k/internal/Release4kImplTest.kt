package com.github.christophpickl.kpotpourri.release4k.internal

import com.github.christophpickl.kpotpourri.common.file.touch
import com.github.christophpickl.kpotpourri.common.io.Io
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuter
import com.github.christophpickl.kpotpourri.github.RepositoryConfig
import com.github.christophpickl.kpotpourri.github.non_test.testInstance
import com.github.christophpickl.kpotpourri.github.non_test.testInstance1
import com.github.christophpickl.kpotpourri.github.non_test.testInstance2
import com.github.christophpickl.kpotpourri.release4k.Release4kException
import com.github.christophpickl.kpotpourri.release4k.Version
import com.github.christophpickl.kpotpourri.release4k.VersionType
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.notNullValue
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.nullValue
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.assertion.assertThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test class Release4kImplTest {

    private val command = "testCommand"

    private lateinit var release4k: Release4kImpl
    private lateinit var process: ProcessExecuter

    @BeforeMethod fun initRelease4k() {
        process = mock<ProcessExecuter>()
        release4k = Release4kImpl(process = process)
    }


    fun `release4kDirectory - sunshine`() {
        val (workingDir, buildDir) = workingAndBuildDir()
        release4k = Release4kImpl(workingDirectory = workingDir, process = process)

        release4k.release4kDirectory.exists() shouldMatchValue true
        release4k.release4kDirectory shouldMatchValue buildDir
        release4k.release4kDirectory.isDirectory shouldMatchValue true
    }

    fun `release4kDirectory - recreates dir`() {
        val (workingDir, buildDir) = workingAndBuildDir()
        val dummyFile = File(buildDir, "testDummy").apply { touch() }

        release4k = Release4kImpl(workingDirectory = workingDir, process = process)

        dummyFile.exists() shouldMatchValue false
    }

    fun `gitCheckoutDirectory - sunshine`() {
        val (workingDir, buildDir) = workingAndBuildDir()
        release4k = Release4kImpl(workingDirectory = workingDir, process = process)

        release4k.gitCheckoutDirectory.exists() shouldMatchValue false

        release4k.gitCheckoutDirectory shouldMatchValue File(buildDir, "git_checkout")

        release4k.gitCheckoutDirectory.exists() shouldMatchValue false
    }

    private fun workingAndBuildDir(): Pair<File, File> {
        val workingDir = File("build/test_build_release4k").apply { deleteRecursively() }
        val buildDir = File(workingDir, "build/release4k")
        return workingDir to buildDir
    }

    fun `initGithub - sunshine`() {
        assertThat(release4k._github, nullValue())

        release4k.initGithub(RepositoryConfig.testInstance)

        assertThat(release4k._github, notNullValue())
    }

    fun `initGithub - twice should fail`() {
        release4k.initGithub(RepositoryConfig.testInstance1)
        assertThrown<Release4kException>(expectedMessageParts = listOf("already invoked", RepositoryConfig.testInstance1.repositoryName)) {
            release4k.initGithub(RepositoryConfig.testInstance2)
        }

    }

    fun `checkoutGitProject - sunshine`() {
        release4k.checkoutGitProject("testGitUrl")

        verify(process).execute("/usr/bin/git", "clone testGitUrl ${release4k.gitCheckoutDirectory.name}", release4k.release4kDirectory)
        verifyNoMoreInteractions(process)

    }

    fun `promptUser - sunshine`() {
        val stdout = Io.readStdoutAndWriteStdin("entered") {
            release4k.promptUser("testPrompt") shouldMatchValue "entered"
        }
        stdout shouldMatchValue "testPrompt\n>> \n"
    }

    @DataProvider
    fun provideVersionTxt(): Array<Array<out Any>> = arrayOf(
            arrayOf("version_1.txt", Version.VersionParts1(VersionType.Release, 1)),
            arrayOf("version_1_2.txt", Version.VersionParts2(VersionType.Release, 1, 2)),
            arrayOf("version_1_2_3.txt", Version.VersionParts3(VersionType.Release, 1, 2, 3)),
            arrayOf("version_1_2_3_4.txt", Version.VersionParts4(VersionType.Release, 1, 2, 3, 4))
    )

    @Test(dataProvider = "provideVersionTxt")
    fun `readVersionFromTxt - sunshine`(fileName: String, expected: Version) {
        release4k.readVersionFromTxt("src/test/resources/$fileName") shouldMatchValue expected
    }

    fun `gradlew - delegates to process executor`() {
        release4k.gradlew(command)

        verify(process).execute("./gradlew", command, release4k.gitCheckoutDirectory)
        verifyNoMoreInteractions(process)
    }

    fun `git - delegates to process executor`() {
        release4k.git(command)

        verify(process).execute("git", command, release4k.gitCheckoutDirectory)
        verifyNoMoreInteractions(process)
    }

    fun `onFinish - delegates say to process executor`() {
        release4k.onFinish()

        verify(process).execute("say", "\"Release build finished.\"", release4k.release4kDirectory, suppressOutput = true)
        verifyNoMoreInteractions(process)
    }

}
