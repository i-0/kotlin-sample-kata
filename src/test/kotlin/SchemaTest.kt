import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.i0.kata.Schema
import org.i0.kata.ParsedSchema
import org.i0.kata.parseSchema
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
        val schema = Schema(options = setOf("o", "p"))

        parseSchema(schema, arrayOf("-p", "someValue")) shouldBe
            ParsedSchema(
                flags = emptyMap(),
                options = mapOf("o" to null, "p" to "someValue"),
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
