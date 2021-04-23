package com.milen.kata.solvers

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class TwoToOneGeneratorTest {
    private lateinit var tested: TwoToOneGenerator

    @Before
    fun setUp() {
        tested = TwoToOneGenerator()
    }

    @Test
    fun `first string is shorter than second`() {
        assertEquals("aehrsty", tested.longest("aretheyhere", "yestheyarehere"))
    }

    @Test
    fun `first string is longer than second`() {
        assertEquals("abcdefghilnoprstu", tested.longest("loopingisfunbutdangerous", "lessdangerousthancoding"))
    }

    @Test
    fun `first string is equal to second`() {
        assertEquals("acefghilnoprstuvy", tested.longest("nylanguagesloveisinthe", "theresapairoffunctions"))
    }
}