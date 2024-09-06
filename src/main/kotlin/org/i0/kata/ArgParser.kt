@file:Suppress("ktlint:standard:filename")

package org.i0.kata

import org.i0.kata.ArgumentTokens.FlagGroup
import org.i0.kata.ArgumentTokens.FlagToken
import org.i0.kata.ArgumentTokens.IdToken
import org.i0.kata.ArgumentTypes.PositionalArgument

fun parseArgs(args: Array<String>, schema: Schema = Schema()): List<ArgumentTypes> =
    parseToken(lexArgs(args), schema)

private fun parseToken(tokens: List<ArgumentTokens>, schema: Schema): List<ArgumentTypes> {
    if (tokens.isEmpty()) return emptyList()

    return when (val first = tokens.first()) {
        is FlagGroup -> first.toFlags() + parseToken(tokens.drop(1), schema)
        is IdToken -> listOf(PositionalArgument(first.value)) + parseToken(tokens.drop(1), schema)
        is FlagToken -> parseFlagToken(tokens, first, schema).let { (arg, rest) -> listOf(arg) + parseToken(rest, schema) }
    }
}

private fun parseFlagToken(
    tokens: List<ArgumentTokens>,
    first: FlagToken,
    schema: Schema
): Pair<ArgumentTypes, List<ArgumentTokens>> {
    val second = tokens.getOrNull(1)
    return when {
        second is IdToken && schema.containsOption(first.flag) ->
            ArgumentTypes.Option(first.flag, second.value) to tokens.drop(2)
        else -> ArgumentTypes.Flag(first.flag) to tokens.drop(1)
    }
}

private fun Schema.containsOption(option: String) = option in options

sealed interface ArgumentTypes {
    data class Flag(
        val name: String,
    ) : ArgumentTypes

    data class Option(
        val name: String,
        val value: String,
    ) : ArgumentTypes

    data class PositionalArgument(
        val value: String,
    ) : ArgumentTypes
}
