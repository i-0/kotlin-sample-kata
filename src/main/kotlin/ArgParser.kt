@file:Suppress("ktlint:standard:filename")

package org.i0.kata

import org.i0.kata.ArgumentTokens.FlagGroup
import org.i0.kata.ArgumentTokens.FlagToken
import org.i0.kata.ArgumentTokens.IdToken
import org.i0.kata.ArgumentTypes.PositionalArgument

fun parseArgs(args: Array<String>): List<ArgumentTypes> = parseToken(lexArgs(args))

private fun parseToken(tokens: List<ArgumentTokens>): List<ArgumentTypes> {
    if (tokens.isEmpty()) return emptyList()

    return when (val first = tokens.first()) {
        is FlagGroup -> first.toFlags() + parseToken(tokens.drop(1))
        is IdToken -> listOf(PositionalArgument(first.value)) + parseToken(tokens.drop(1))
        is FlagToken -> parseFlagToken(tokens, first).let { (arg, rest) -> listOf(arg) + parseToken(rest) }
    }
}

private fun parseFlagToken(
    tokens: List<ArgumentTokens>,
    first: FlagToken,
): Pair<ArgumentTypes, List<ArgumentTokens>> =
    when (val second = tokens.getOrNull(1)) {
        is IdToken -> ArgumentTypes.Option(first.flag, second.value) to tokens.drop(2)
        else -> ArgumentTypes.Flag(first.flag) to tokens.drop(1)
    }

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
