package com.milen.kata.solvers

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class FactorialDecomposerTest {


    private lateinit var tested: FactorialDecomposer

    @Before
    fun setUp() {
        tested = FactorialDecomposer()
    }

    @Test
    fun test1() {
        doTest(17, "2^15 * 3^6 * 5^3 * 7^2 * 11 * 13 * 17")
    }

    @Test
    fun test2() {
        doTest(5, "2^3 * 3 * 5")
    }

    @Test
    fun test3() {
        doTest(22, "2^19 * 3^9 * 5^4 * 7^3 * 11^2 * 13 * 17 * 19")
    }

    @Test
    fun test4() {
        doTest(14, "2^11 * 3^5 * 5^2 * 7^2 * 11 * 13")

    }

    @Test
    fun test5() {
        doTest(25, "2^22 * 3^10 * 5^6 * 7^3 * 11^2 * 13 * 17 * 19 * 23")
    }


    private fun doTest(n:Int, expect:String) {
        val result = tested.decomp(n)
        assertEquals(expect, result)
    }
}