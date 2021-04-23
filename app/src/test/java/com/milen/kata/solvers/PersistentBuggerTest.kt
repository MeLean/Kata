package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PersistentBuggerTest{

    private lateinit var tested: PersistentBugger

    @Before
    fun setUp() {
        tested = PersistentBugger()
    }

    @Test
    fun `input 4 expected 0`() {
        doTest(4,0)
    }

    @Test
    fun `input 25 expected 2`() {
        doTest(25,2)
    }

    @Test
    fun `input 39 expected 3`() {
       doTest(39,3)
    }

    @Test
    fun `input 999 expected 4`() {
        doTest(999,4)
    }

    private fun doTest(n: Int, exp: Int) {
        //val result = tested.persistence(n)
        val result = tested.shortPersistence(n)
        assertEquals(exp, result)
    }
}