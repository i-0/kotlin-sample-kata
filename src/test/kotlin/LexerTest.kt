import ArgumentTokens.FlagGroup
import ArgumentTokens.FlagToken
import ArgumentTokens.IdToken
import ArgumentTypes.Flag
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.text.toIntOrNull

class LexerTest {
    @Test
    fun `empty args result in an empty map`() {
        val args = emptyArray<String>()
        lexArgs(args) shouldBe emptyList()
    }

    @Test
    fun `one flag is put into the map`() {
        val args = arrayOf("-f")
        lexArgs(args) shouldBe listOf(FlagToken("f"))
    }

    @Test
    fun `flag with fully qualified name is put into the map`() {
        val args = arrayOf("--flag")
        lexArgs(args) shouldBe listOf(FlagToken("flag"))
    }

    @Test
    fun `multiple flags long form`() {
        val args = arrayOf("-f", "-e")
        lexArgs(args) shouldBe listOf(FlagToken("f"), FlagToken("e"))
    }

    @Test
    fun `multiple combined flags`() {
        val args = arrayOf("-fe")
        lexArgs(args) shouldBe listOf(FlagGroup(FlagToken("f"), FlagToken("e")))
    }

    @Test
    fun `mixing long and short flags`() {
        val args = arrayOf("--flag1", "-das", "--flag2")
        lexArgs(args) shouldBe
            listOf(
                FlagToken("flag1"),
                FlagGroup(FlagToken("d"), FlagToken("a"), FlagToken("s")),
                FlagToken("flag2"),
            )
    }

    @Test
    fun `lex single positional argument`() {
        val args = arrayOf("posArg")
        lexArgs(args) shouldBe listOf(IdToken("posArg"))
    }

    @Test
    fun `lex single option`() {
        val args = arrayOf("--option", "ovalue")
        lexArgs(args) shouldBe listOf(FlagToken("option"), IdToken("ovalue"))
    }

    @Test
    fun `positional arguments`() {
        val args = arrayOf("--option1", "o1", "posArg1", "posArg2")
        lexArgs(args) shouldBe listOf(FlagToken("option1"), IdToken("o1"), IdToken("posArg1"), IdToken("posArg2"))
    }
}

fun lexArgs(args: Array<String>): List<ArgumentTokens> =
    args.map { argument: String ->
        when {
            argument.isInteger() -> IdToken(argument)
            argument.isFullyQualifiedFlag() -> FlagToken(argument.removePrefix("--"))
            argument.isFlag() -> FlagToken(argument.removePrefix("-"))
            argument.isFlagGroup() -> FlagGroup(argument.removePrefix("-").map { FlagToken("$it") })
            else -> IdToken(argument)
        }
    }

private fun String.isInteger() = this.toIntOrNull() != null
private fun String.isFullyQualifiedFlag() = this.startsWith("--")
private fun String.isFlag() = this matches "-\\w".toRegex()
private fun String.isFlagGroup() = this.startsWith("-")

sealed interface ArgumentTokens {
    data class FlagToken(
        val flag: String,
    ) : ArgumentTokens

    data class IdToken(
        val value: String,
    ) : ArgumentTokens

    data class FlagGroup(
        val flags: List<FlagToken>,
    ) : ArgumentTokens {
        constructor(vararg flags: FlagToken) : this(flags.toList())

        fun toFlags() = flags.map { Flag(it.flag) }
    }
}
