package com.milen.kata.solvers

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before

class PrimeNumbersTest {

    private lateinit var tested: PrimeNumbers

    @Before
    fun setUp() {
        tested = PrimeNumbers()
    }

    @Test
    fun test1() {
        doTest(1024, "(2**10)")
    }

    @Test
    fun test2() {
        doTest(7775460, "(2**2)(3**3)(5)(7)(11**2)(17)")
    }

    private fun doTest(n: Int, exp: String) {
        val result = tested.factors(n)
        assertEquals(exp, result)
    }
}