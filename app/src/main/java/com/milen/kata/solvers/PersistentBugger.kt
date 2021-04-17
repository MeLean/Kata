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

    private fun multiply(num: Int): Int {
        var multiplyRes = 1
        val numStr = num.toString()
        for (element in numStr){
            multiplyRes *= Character.getNumericValue(element)
        }

        return multiplyRes
    }
}