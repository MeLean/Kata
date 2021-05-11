package com.milen.kata.solvers

class StringIncrementer {

    /*
        Your job is to write a function which increments a string, to create a new string.

        If the string already ends with a number, the number should be incremented by 1.
        If the string does not end with a number. the number 1 should be appended to the new string.
        Examples:

        foo -> foo1

        foobar23 -> foobar24

        foo0042 -> foo0043

        foo9 -> foo10

        foo099 -> foo100

        Attention: If the number has leading zeros the amount of digits should be considered.
    */
    fun incrementString(str: String): String =
        when {
            str.isEmpty() -> "1"
            else -> incrementNonEmptyStr(str, str.takeLastWhile { it.isDigit() })
        }


    private fun incrementNonEmptyStr(str: String, digitsAsStr: String): String = when {
        digitsAsStr.isEmpty() -> "${str}1" //no num found
        else -> {
            val numIncStr = (digitsAsStr.toInt() + 1).toString()
            val leadingZerosCount = digitsAsStr.length - numIncStr.length

            val leadingZerosStr = when {
                leadingZerosCount > 0 -> "0".repeat(leadingZerosCount)
                else -> ""
            }

            "${str.dropLast(digitsAsStr.length)}$leadingZerosStr$numIncStr"
        }
    }
}