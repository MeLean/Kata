package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ThreeFiveMultiplierTest{

    private lateinit var tested: ThreeFiveMultiplier

    @Before
    fun setUp() {
        tested = ThreeFiveMultiplier()
    }

    @Test
    fun test1() {
        doTest(10, 23)
    }

    @Test
    fun test2() {
        doTest(20, 78)
    }

    @Test
    fun test3() {
        doTest(200, 9168)
    }

    private fun doTest(n:Int, expect:Int) {
        val result = tested.solution(n)
        assertEquals(expect, result)
    }

}