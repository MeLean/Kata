package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringIncrementerTest{
    private lateinit var tested: StringIncrementer

    @Before
    fun setUp() {
        tested = StringIncrementer()
    }

    @Test
    fun `should increase string only with leading zeros`(){
        assertEquals(tested.incrementString("foobar000"), "foobar001")
    }

    @Test
    fun `should increase string with leading zeros ends with number`(){
        assertEquals(tested.incrementString("foobar001"), "foobar002");
    }

    @Test
    fun `should increase string without numbers`(){
        assertEquals(tested.incrementString("foo"), "foo1")
    }

    @Test
    fun `should increase string with leading zeros by overlapping digit count`(){
        assertEquals(tested.incrementString("foobar099"), "foobar100")
    }

    @Test
    fun `should increase string with leading zeros and zero in the number`(){
        assertEquals(tested.incrementString("foobar0109"), "foobar0110")
    }

    @Test
    fun `should increase string without leading zeros by overlapping digit count`(){
        assertEquals(tested.incrementString("foobar99"), "foobar100")
    }

    @Test
    fun `should return one if string is empty`(){
        assertEquals(tested.incrementString(""), "1")
    }
}