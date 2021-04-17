package com.milen.kata.solvers

class PersistentBugger{

    fun persistence(num: Int) : Int {
        var counter = 0

        var result = num
        while (result > 9){
            counter++
            result = multiply(result)
        }

        return counter
    }

    fun shortPersistence(num: Int) : Int = generateSequence(num) {
        it.toString().map(Character::getNumericValue).reduce { mult, element -> mult * element }
    }.takeWhile { it > 9 }.count()

    private fun multiply(num: Int): Int {
        var multiplyRes = 1
        val numStr = num.toString()
        for (element in numStr){
            multiplyRes *= Character.getNumericValue(element)
        }

        return multiplyRes
    }
}