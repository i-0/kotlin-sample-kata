package org.i0.kata

import org.i0.kata.ArgumentTokens.FlagGroup
import org.i0.kata.ArgumentTokens.FlagToken
import org.i0.kata.ArgumentTokens.IdToken
import org.i0.kata.ArgumentTypes.Flag

fun lexArgs(args: Array<String>): List<ArgumentTokens> =
    args.map { argument: String ->
        when {
            argument.isInteger() -> IdToken(argument)
            argument.isFullyQualifiedFlag() -> FlagToken(argument.removePrefix("--"))
            argument.isFlag() -> FlagToken(argument.removePrefix("-"))
            argument.isFlagGroup() ->
                FlagGroup(
                    argument.removePrefix("-").map {
                        FlagToken("$it")
                    },
                )
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
