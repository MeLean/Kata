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
    fun test0(){
        doTest("abcde", 0)
    }

    @Test
    fun test1(){
        doTest("abcdea", 1)
    }

    @Test
    fun test2(){
        doTest("indivisibility", 1)
    }

    @Test
    fun test3(){
        doTest("Aaghtyuosbb", 2)
    }

    @Test
    fun test4(){
        doTest("baghtyuosB", 1)
    }

    @Test
    fun test5(){
        doTest("dA" + "c".repeat(10) + "b".repeat(100) + "a".repeat(1000) , 3)
    }

    private fun doTest(input:String, expect:Int) {
        val result = tested.duplicateCount(input)
        assertEquals(expect, result)
    }
}