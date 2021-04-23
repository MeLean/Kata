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
    fun `input 10 expected 23`() {
        doTest(10, 23)
    }

    @Test
    fun `input 20 expected 78`() {
        doTest(20, 78)
    }

    @Test
    fun `input 200 expected 9168`() {
        doTest(200, 9168)
    }

    private fun doTest(n:Int, expect:Int) {
        val result = tested.solution(n)
        assertEquals(expect, result)
    }

}