import io.kotest.matchers.shouldBe
import org.i0.kata.ArgumentTypes.Flag
import org.i0.kata.ArgumentTypes.Option
import org.i0.kata.ArgumentTypes.PositionalArgument
import org.i0.kata.parseArgs
import org.junit.jupiter.api.Test

class ArgParserTest {
    // foo-bar -f -l 1 hello
    // l to 1, hello to null
    @Test
    fun `empty args result in an empty map`() {
        val args = emptyArray<String>()
        parseArgs(args) shouldBe emptyList()
    }

    @Test
    fun `one flag is put into the map`() {
        val args = arrayOf("-f")
        parseArgs(args) shouldBe listOf(Flag("f"))
    }

    @Test
    fun `flag with fully qualified name is put into the map`() {
        val args = arrayOf("--flag")
        parseArgs(args) shouldBe listOf(Flag("flag"))
    }

    @Test
    fun `multiple flags long form`() {
        val args = arrayOf("-f", "-e")
        parseArgs(args) shouldBe listOf(Flag("f"), Flag("e"))
    }

    @Test
    fun `multiple combined flags`() {
        val args = arrayOf("-fe")
        parseArgs(args) shouldBe listOf(Flag("f"), Flag("e"))
    }

    @Test
    fun `mixing long and short flags`() {
        val args = arrayOf("--flag1", "-das", "--flag2")
        parseArgs(args) shouldBe listOf(Flag("flag1"), Flag("d"), Flag("a"), Flag("s"), Flag("flag2"))
    }

    @Test
    fun `parse single positional argument`() {
        val args = arrayOf("posArg")
        parseArgs(args) shouldBe listOf(PositionalArgument("posArg"))
    }

    @Test
    fun `parse single option`() {
        val args = arrayOf("--option", "ovalue")
        parseArgs(args) shouldBe listOf(Option("option", "ovalue"))
    }

    @Test
    fun `positional arguments`() {
        val args = arrayOf("--option1", "o1", "posArg1", "posArg2")
        parseArgs(args) shouldBe
            listOf(
                Option("option1", "o1"),
                PositionalArgument("posArg1"),
                PositionalArgument("posArg2"),
            )
    }

    @Test
    fun `parse -9  as positional arg `() {
        val args = arrayOf("-9")
        parseArgs(args) shouldBe listOf(PositionalArgument("-9"))
    }

    @Test
    fun `parse -9  as option`() {
        val args = arrayOf("--pid", "-9")
        parseArgs(args) shouldBe listOf(Option("pid", "-9"))
    }

    @Test
    fun `parse -987  as option`() {
        val args = arrayOf("--pid", "-987")
        parseArgs(args) shouldBe listOf(Option("pid", "-987"))
    }

    @Test
    fun `parse --987 as a flag with full-qualified name 987`() {
        val args = arrayOf("--flag1", "--987")
        parseArgs(args) shouldBe listOf(Flag("flag1"), Flag("987"))
    }
}
