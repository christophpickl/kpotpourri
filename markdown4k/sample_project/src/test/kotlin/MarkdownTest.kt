import com.github.christophpickl.kpotpourri.markdown4k.MarkdownTestngTest
import org.testng.annotations.Test
import java.io.File

@Test
class MarkdownTest : MarkdownTestngTest(
        root = File("."),
        ignoreFolders = listOf("src", "build", ".git", ".gradle", "out")
)
