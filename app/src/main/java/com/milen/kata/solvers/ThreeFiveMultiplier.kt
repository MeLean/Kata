package com.milen.kata.solvers

class ThreeFiveMultiplier {
/*
    If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
    The sum of these multiples is 23.

    Finish the solution so that it returns the sum of all the multiples of 3 or 5 below the number passed in.

    Note: If the number is a multiple of both 3 and 5, only count it once.
    Also, if a number is negative, return 0(for languages that do have them)
*/

    fun solution(number: Int): Int {
        val n1 = 3
        val n2 = 5
        var result = 0
        var multiplyer = 1
        var couldHaveSolution = n1 * multiplyer < number

        while (couldHaveSolution) {
            val curPossibleNum = n1 * multiplyer
            //check if the current curPossibleNum is also
            //could be divided by n2
            if(curPossibleNum % n2 != 0){
                result += (n1 * multiplyer)
            }

            if (n2 * multiplyer < number) {
                result += (n2 * multiplyer)
            }

            multiplyer++
            couldHaveSolution = n1 * (multiplyer) < number
        }

        return result
    }
}