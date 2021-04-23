package com.milen.kata.solvers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MumblingTest{
    private lateinit var tested: Mumbling

    @Before
    fun setUp() {
        tested = Mumbling()
    }

    @Test
    fun `input string start with lower case`(){
        doTest(
            "zpglnRxqenU" ,
            "Z-Pp-Ggg-Llll-Nnnnn-Rrrrrr-Xxxxxxx-Qqqqqqqq-Eeeeeeeee-Nnnnnnnnnn-Uuuuuuuuuuu")

    }

    @Test
    fun `input string start with upper case`(){
        doTest(
            "NyffsGeyylB" ,
            "N-Yy-Fff-Ffff-Sssss-Gggggg-Eeeeeee-Yyyyyyyy-Yyyyyyyyy-Llllllllll-Bbbbbbbbbbb")

    }

    @Test
    fun `empty string input`(){
        doTest(
            "" ,
            "")

    }


    private fun doTest(input:String, expect:String) {
        val result = tested.accum(input)
        assertEquals(expect, result)
    }
}