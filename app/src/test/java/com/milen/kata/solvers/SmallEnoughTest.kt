package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SmallEnoughTest{

    private lateinit var tested: SmallEnough

    @Before
    fun setUp() {
        tested = SmallEnough()
    }

    @Test
    fun `should return true all numbs are smaller`() {
        assertEquals(true, tested.smallEnough(intArrayOf(66, 101), 200))
    }

    @Test
    fun `should return false there are grater numbs`() {
        assertEquals(false,tested.smallEnough(intArrayOf(78, 117, 110, 99, 104, 117, 107, 115), 100))
    }

    @Test
    fun `should return true all numbs are smaller or equals`() {
        assertEquals(true, tested.smallEnough(intArrayOf(101, 45, 75, 105, 99, 107), 107))
    }

    @Test
    fun `should return true all numbs are smaller with repeating`() {
        assertEquals(true, tested.smallEnough(intArrayOf(80, 117, 115, 104, 45, 85, 112, 115), 120))
    }
}