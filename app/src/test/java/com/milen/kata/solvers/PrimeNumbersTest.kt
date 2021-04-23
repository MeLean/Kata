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
    fun `input small number`() {
        doTest(1024, "(2**10)")
    }

    @Test
    fun `input big number`() {
        doTest(7775460, "(2**2)(3**3)(5)(7)(11**2)(17)")
    }

    @Test
    fun `input 0 expected empty string`() {
        doTest(0, "")
    }

    private fun doTest(n: Int, exp: String) {
        val result = tested.factors(n)
        assertEquals(exp, result)
    }
}