# Markdown4k

Writing documentation is a good idea, right, we agree on that? So you end up having tons of markdown files with tons of kotlin code snippets.
After a while they get outdated, because writing documentation is good, but who cares about updating them?!
This is where __Markdown4k__ gets handy, as it tries to compile all of your code snippets and reports an error if something wrent wrong. 
 
The suggested approach is to integrate the check into a testing framework like TestNG or JUnit or whatever you prefer.

The output on failure is fine tuned to expose enough information to immediately figure out what's going on (using TestNG and Gradle):

```
Gradle suite > Gradle test > com.package.MyMakrdown4kTest.testMethodName[2](CodeSnippet{relativePath=/README.md, lineNumber=78, code=val root = ...}) FAILED
    javax.script.ScriptException: Error: error: unresolved reference: File
        val root = File("./")
                   ^
```

## Usage

Basically you setup a data providing method which returns the collected snippets,
afterwards consume all of them and try to compile them.

The following examples assume all relevant markdown files reside in the current working directory.

### TestNG

Use TestNG's `@DataProvider` mechanism in combination with a handy `toDataProviding()` 
extension method in order to convert the returned `List` into something TestNG can actually use.

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.test4k.toDataProviding
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File

@Test class UsageTestngTest {

    @DataProvider
    fun provideSnippets() = 
        Markdown4k.collectSnippets(root = File(".")).toDataProviding()

    @Test(dataProvider = "provideSnippets")
    fun `compiling markdown should not throw exception`(snippet: CodeSnippet) {
        Markdown4k.compile(snippet)
    }

}
```

![Markdown4k Screenshot](https://github.com/christophpickl/kpotpourri/raw/master/doc/images/markdown4k-screenshot_intellij_run.png)

## JUnit

Use JUnit's parameterized tests in combination with a handy `toParamterized()` 
extension method in order to convert the returned `List` into something JUnit can actually use.

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.CodeSnippet
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
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
            Markdown4k.collectSnippets(root = File(".")).toParamterized()
    }

    @Test
    fun `compiling markdown should not throw exception`() {
        Markdown4k.compile(snippet)
    }

}
```

## Hints

### Ignore folders from scan

The `collectSnippets()` provides a default argument which enables you to ignore certain directories by their name:

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import java.io.File

Markdown4k.collectSnippets(
    root = File("./"),
    ignoreFolders = listOf("src", "build", ".git")
)
```
    
### Declare a code snippet to be ignored

Start a line with `/// unsafe` in order to ignore this code snippet for compilation:

    ```kotlin
    /// unsafe
    val x = notCompilable
    ```

## Troubleshooting

* Remember to add all **import statements** which are not included by default.
* The markdown4k executing test has to reference ANY **dependency** which is required by the imported types along ALL markdown files.
* In order to be picked up as a valid Kotlin snippet, the line must not have leading or trailing **whitespace**.
    * This enables you to indent your markdown snippet by 4 spaces which is another approach to ignore a specific snippet ;)
* As the code gets compiled via using a script engine, executing many tests could **slow**  down the build drastically.
