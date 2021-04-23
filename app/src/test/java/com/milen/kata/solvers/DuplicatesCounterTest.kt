package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DuplicatesCounterTest{
    private lateinit var tested: DuplicatesCounter

    @Before
    fun setUp() {
        tested = DuplicatesCounter()
    }

    @Test
    fun `input with no duplicates`(){
        doTest("abcde", 0)
    }

    @Test
    fun `input with one couple duplicate`(){
        doTest("abcdea", 1)
    }

    @Test
    fun `input with more then 2 same duplicates`(){
        doTest("indivisibility", 1)
    }

    @Test
    fun `input with uppercase and lowercase duplicates`(){
        doTest("Aaghtyuosbb", 2)
    }

    @Test
    fun `input with uppercase and lowercase duplicates at the end`(){
        doTest("baghtyuosB", 1)
    }

    @Test
    fun `input with big amount of duplicates`(){
        doTest("dA" + "c".repeat(10) + "b".repeat(100) + "a".repeat(1000) , 3)
    }

    private fun doTest(input:String, expect:Int) {
        val result = tested.duplicateCount(input)
        assertEquals(expect, result)
    }
}