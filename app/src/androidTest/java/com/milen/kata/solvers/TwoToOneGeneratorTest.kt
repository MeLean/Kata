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
    fun test1() {
        assertEquals("aehrsty", tested.longest("aretheyhere", "yestheyarehere"))
    }

    @Test
    fun test2() {
        assertEquals("abcdefghilnoprstu", tested.longest("loopingisfunbutdangerous", "lessdangerousthancoding"))
    }

    @Test
    fun test3() {
        assertEquals("acefghilmnoprstuy", tested.longest("inmanylanguages", "theresapairoffunctions"))
    }
}