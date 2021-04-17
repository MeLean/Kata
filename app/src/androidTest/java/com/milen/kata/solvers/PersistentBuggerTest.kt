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
    fun test1() {
       doTest(39,3)
    }

    @Test
    fun test2() {
        doTest(4,0)
    }

    @Test
    fun test3() {
        doTest(25,2)
    }

    @Test
    fun test5() {
        doTest(999,4)
    }

    private fun doTest(n: Int, exp: Int) {
        val result = tested.persistence(n)
        assertEquals(exp, result)
    }
}