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
    fun `input one word should be reversed`() {
        assertEquals("emocleW", tested.spinWords("Welcome"))
    }


    @Test
    fun `input many words last should be reversed first should not`() {
        assertEquals("Hey wollef sroirraw", tested.spinWords("Hey fellow warriors"))
    }

    @Test
    fun `input many words should not be reversed`() {
        assertEquals("This is a test", tested.spinWords("This is a test"))
    }

    @Test
    fun `input many words third should be reversed others should not`() {
        assertEquals("This is rehtona but last test", tested.spinWords("This is another but last test"))
    }

    @Test
    fun `input many words second, third and fifth should be reversed others should not`() {
        assertEquals(
            "Just gniddik ereht is llits one more",
            tested.spinWords("Just kidding there is still one more")
        )
    }
}