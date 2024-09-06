package org.i0.kata

data class ParsedSchema(
    val flags: Map<String, Boolean>,
    val options: Map<String, String?>,
    val positionalArguments: List<String>,
)

data class Schema(
    val flags: Set<String> = emptySet(),
    val options: Set<String> = emptySet(),
    val positionalArgument: Int = 0,
)

fun parseSchema(
    schema: Schema,
    args: Array<String>,
): ParsedSchema {
    val arguments = parseArgs(args, schema)
    val parsedArguments =
        ParsedSchema(
            flags = arguments.filterIsInstance<ArgumentTypes.Flag>().associate { it.name to true },
            options = arguments.filterIsInstance<ArgumentTypes.Option>().associate { it.name to it.value },
            positionalArguments = arguments.filterIsInstance<ArgumentTypes.PositionalArgument>().map { it.value },
        )

    require(schema.flags.containsAll(parsedArguments.flags.keys)) {
        "Parsed arguments contain unsupported flags ${parsedArguments.flags.keys - schema.flags}"
    }
    require(parsedArguments.positionalArguments.size == schema.positionalArgument) {
        "Expected ${schema.positionalArgument} positional arguments, got ${parsedArguments.positionalArguments.size}"
    }

    return ParsedSchema(
        flags = schema.flags.associateWith { parsedArguments.flags.getOrDefault(it, false) },
        options = schema.options.associateWith { parsedArguments.options[it] },
        positionalArguments = parsedArguments.positionalArguments
    )
}