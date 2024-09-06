import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.i0.kata.ArgumentTypes.Flag
import org.i0.kata.ArgumentTypes.Option
import org.i0.kata.ArgumentTypes.PositionalArgument
import org.i0.kata.parseArgs
import kotlin.test.Test

class SchemaTest {
    @Test
    fun `a flag is parsed`() {
        val schema = Schema(flags = setOf("f"))

        parseSchema(schema, arrayOf("-f")) shouldBe
            ParsedSchema(
                flags = mapOf("f" to true),
                options = emptyMap(),
                positionalArguments = emptyList(),
            )
    }

    @Test
    fun `an unset flag is parsed`() {
        val schema = Schema(flags = setOf("f"))

        parseSchema(schema, arrayOf("-g")) shouldBe
            ParsedSchema(
                flags = mapOf("f" to false),
                options = emptyMap(),
                positionalArguments = emptyList(),
            )
    }

    @Test
    fun `an option is parsed`() {
        val schema = Schema(options = setOf("o"))

        parseSchema(schema, arrayOf("-o", "value")) shouldBe
            ParsedSchema(
                flags = emptyMap(),
                options = mapOf("o" to "value"),
                positionalArguments = emptyList(),
            )
    }

    @Test
    fun `an unset option is parsed to null`() {
        val schema = Schema(options = setOf("o"))

        parseSchema(schema, arrayOf("-p", "someValue")) shouldBe
            ParsedSchema(
                flags = emptyMap(),
                options = mapOf("o" to null),
                positionalArguments = emptyList(),
            )
    }

    @Test
    fun `positional arguments are set`() {
        val schema = Schema(positionalArgument = 1)

        parseSchema(schema, arrayOf("posArg")) shouldBe
            ParsedSchema(
                flags = emptyMap(),
                options = emptyMap(),
                positionalArguments = listOf("posArg"),
            )
    }

    @Test
    fun `positional arguments are required`() {
        val schema = Schema(positionalArgument = 1)

        shouldThrow<IllegalArgumentException> {
            parseSchema(schema, emptyArray())
        }
    }

    @Test
    fun `Mixed arguments`() {
        val schema =
            Schema(
                flags = setOf("f"),
                options = setOf("o"),
                positionalArgument = 1,
            )

        parseSchema(schema, arrayOf("-f", "-o", "value", "posArg")) shouldBe
            ParsedSchema(
                flags = mapOf("f" to true),
                options = mapOf("o" to "value"),
                positionalArguments = listOf("posArg"),
            )
    }

    @Test
    fun `Flag with positional argument`() {
        val schema =
            Schema(
                flags = setOf("f"),
                options = emptySet(),
                positionalArgument = 1,
            )

        parseSchema(schema, arrayOf("-f", "posArg")) shouldBe
            ParsedSchema(
                flags = mapOf("f" to true),
                options = emptyMap(),
                positionalArguments = listOf("posArg"),
            )
    }
}

fun parseSchema(
    schema: Schema,
    args: Array<String>,
): ParsedSchema {
    val arguments = parseArgs(args)
    val parsedArguments =
        ParsedSchema(
            flags = arguments.filterIsInstance<Flag>().associate { it.name to true },
            options = arguments.filterIsInstance<Option>().associate { it.name to it.value },
            positionalArguments = arguments.filterIsInstance<PositionalArgument>().map { it.value },
        )

    return ParsedSchema(
        flags = schema.flags.associateWith { parsedArguments.flags.getOrDefault(it, false) },
        options = schema.options.associateWith { parsedArguments.options[it] },
        positionalArguments =
            parsedArguments.positionalArguments.also {
                if (it.size != schema.positionalArgument) {
                    throw IllegalArgumentException("Expected ${schema.positionalArgument} positional arguments, got ${it.size}")
                }
            },
    )
}

data class Schema(
    val flags: Set<String> = emptySet(),
    val options: Set<String> = emptySet(),
    val positionalArgument: Int = 0,
)

data class ParsedSchema(
    val flags: Map<String, Boolean>,
    val options: Map<String, String?>,
    val positionalArguments: List<String>,
)
