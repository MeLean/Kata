package com.milen.kata.solvers

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WordSpinnerTest {
    private lateinit var tested: WordSpinner

    @Before
    fun setUp() {

        tested = WordSpinner()
    }

    @Test
    fun test1() {
        assertEquals("emocleW", tested.spinWords("Welcome"))
    }


    @Test
    fun test2() {
        assertEquals("Hey wollef sroirraw", tested.spinWords("Hey fellow warriors"))
    }

    @Test
    fun test3() {
        assertEquals("This is a test", tested.spinWords("This is a test"))
    }

    @Test
    fun test4() {
        assertEquals("This is rehtona test", tested.spinWords("This is another test"))
    }

    @Test
    fun test5() {
        assertEquals(
            "You are tsomla to the last test",
            tested.spinWords("You are almost to the last test")
        )
    }

    @Test
    fun test6() {
        assertEquals(
            "Just gniddik ereht is llits one more",
            tested.spinWords("Just kidding there is still one more")
        )
    }
}