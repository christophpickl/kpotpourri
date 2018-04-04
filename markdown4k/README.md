# Markdown4k

Writing documentation is a good idea, we agree on that, right?

So you end up having tons of (Markdown) files with tons of Kotlin code snippets.
After a while they get outdated, because writing documentation is good, but who cares about updating them, right?! ;)

This is where Markdown4k gets handy, as it tries to **compile all of your Kotlin code snippets** and reports an error if something wrent wrong.

## Usage

The suggested approach is to integrate the check into a **testing framework** like TestNG or JUnit or whatever you prefer,
but first of all you need to declare a new dependency for markdown4k in your `build.gradle` file along with the additional repositories for kpotpourri and the kotlin scripting engine:


```groovy
dependencies {
  testCompile group: "com.github.christophpickl.kpotpourri", name: "markdown4k", version: versionKPotpourri, classifier: "tests"
}

repositories {
    mavenCentral()
    maven { url "http://dl.bintray.com/christophpickl/cpickl" }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}
```

Afterwards you only need to feed the test framework with your code snippets by passing a `root` directory which will be scanned for `*.md` files recursively,
and then try to compile them and check for a proper `KompilationResult`.

(The following examples assume all relevant markdown files reside in the current working directory)

For a complete sample usage, I've prepared the sample project in the `sample_project/` directory.

### Manual invocation

The quickest way to trigger a compilation cycle is by collecting and compiling the snippets:

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.markdown4k.KompilationResult
import java.io.File

Markdown4k.kollect(File("root_directory")).forEach { 
    val result: KompilationResult = Markdown4k.kompile(it)
    // do something with [ Success, Failure(ScriptException), Ignored ]
}
```

PS: The snippet above is checked by markdown4k of course.

### TestNG

For sake of simplicity there is a ready to use base class which only requires you to pass
a root file (under the hood the base class is using TestNG's `@DataProvider` mechanism):

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.MarkdownTestngTest
import org.testng.annotations.Test
import java.io.File

@Testclass UsageTestngTest : MarkdownTestngTest(
    root = File(".")
)
```

![IntelliJ Screenshot using TestNG](https://github.com/christophpickl/kpotpourri/raw/master/doc/images/markdown4k-screenshot_intellij_run.gif)

## JUnit

Here we are using JUnit's parameterized tests in combination with a custom `toParamterized()` 
extension method in order to convert the returned `List` into something JUnit can actually use:

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.markdown4k.assertKompileSuccessOrIgnored
import com.github.christophpickl.kpotpourri.test4k.toParamterized
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

@RunWith(Parameterized::class)
class UsageJUnitTest(private val snippet: CodeSnippet) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: compile {0}")
        fun data() =
            Markdown4k.kollect(root = File(".")).toParamterized()
    }

    @Test
    fun `compiling markdown should not throw exception`() {
        assertKompileSuccessOrIgnored(snippet)
    }

}
```

## Hints

### Ignore folders

The `kollect()` method provides a default argument which enables you to ignore certain directories by their name called `ignoreFolders`:

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import java.io.File

val codeSnippets = Markdown4k.kollect(
    root = File("./"),
    ignoreFolders = listOf("src", "build", ".git")
)
```
    
### Ignore code snippets

Start a line with `/// ignore` in your markdown files, in order to exclude this specific code snippet from compilation:

    ```kotlin
    /// ignore
    val x = notCompilable
    ```

## Troubleshooting

* Remember to add all **import statements** which are not included by default.
* Whenever you import a type in your code snippet, this type has to be made available by adding a proper `testRuntime` **dependency**.
    * Using multi module builds and outsourcing the markdown check into its own submodule, requires you to add additional `:project` dependencies.
* In order to be picked up as a valid Kotlin snippet, the line must not have leading or trailing **whitespace**.
    * This enables you to indent your markdown snippet by 4 spaces which is another approach to ignore a specific snippet ;)
* As the code gets compiled via using a script engine, executing many tests could **slow**  down the build drastically.
