package com.milen.kata.solvers

class TwoToOneGenerator {
/*    Take 2 strings s1 and s2 including only letters from ato z. Return a new sorted string, the longest possible, containing distinct letters - each taken only once - coming from s1 or s2.

    Examples:
    a = "xyaabbbccccdefww"
    b = "xxxxyyyyabklmopq"
    longest(a, b) -> "abcdefklmopqwxy"

    a = "abcdefghijklmnopqrstuvwxyz"
    longest(a, a) -> "abcdefghijklmnopqrstuvwxyz"
*/

    fun longest(s1:String, s2:String):String {
        val result = StringBuilder()

        result.append(s1)
        result.append(s2)

        return result.toSortedSet().joinToString(separator="")
    }
}