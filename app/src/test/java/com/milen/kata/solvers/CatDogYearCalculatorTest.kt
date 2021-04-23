package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CatDogYearCalculatorTest{

    private lateinit var tested: CatDogYearCalculator

    @Before
    fun setUp() {
        tested = CatDogYearCalculator()
    }

    @Test
    fun `one year`() {
        assertArrayEquals(arrayOf(1, 15, 15), tested.calculateYears(1));
    }

    @Test fun  `two years`() {
        assertArrayEquals(arrayOf(2, 24, 24), tested.calculateYears(2));
    }

    @Test fun `ten years`() {
        assertArrayEquals(arrayOf(10, 56, 64), tested.calculateYears(10));
    }
}