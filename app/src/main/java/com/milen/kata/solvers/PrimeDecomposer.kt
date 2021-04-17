package com.milen.kata.solvers

class PrimeDecomposer {

    /*
    Given a positive number n > 1 find the prime factor decomposition of n. The result will be a string with the following form :
    "(p1**n1)(p2**n2)...(pk**nk)"
    where a ** b means a to the power of b
    with the p(i) in increasing order and n(i) empty if n(i) is 1.
    Example: n = 86240 should return "(2**5)(5)(7**2)(11)"
    */

    fun factors(l: Int): String {
        var value = l
        val results: MutableMap<Int, Int> = mutableMapOf()

        var curPrime = 2
        while (value >= curPrime) {
            if (value % curPrime == 0) {
                value /= curPrime

                if (results.containsKey(curPrime)) {
                    val nextValue = results[curPrime]?.plus(1) ?: 1
                    results[curPrime] = nextValue
                } else {
                    results[curPrime] = 1
                }
            } else {
                curPrime = getNextPrime(curPrime)
            }
        }

        return printStrResult(results).toString()
    }

    private fun printStrResult(results: MutableMap<Int, Int>): StringBuilder {
        val resultsStr = StringBuilder()
        results.keys.forEach {
            if (results[it] ?: 0 <= 1) {
                resultsStr.append("($it)")
            } else {
                resultsStr.append("($it**${results[it]})")
            }
        }
        return resultsStr
    }

    private fun getNextPrime(curPrime: Int): Int {
        var result = curPrime + 1
        while (!isPrime(result)) {
            result++
        }

        return result
    }

    private fun isPrime(num: Int): Boolean {
        var flag = true
        for (i in 2..num / 2) {
            if (num % i == 0) {
                flag = false
                break
            }
        }

        return flag
    }
}