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
    fun test1(){
        doTest(
            "ZpglnRxqenU" ,
            "Z-Pp-Ggg-Llll-Nnnnn-Rrrrrr-Xxxxxxx-Qqqqqqqq-Eeeeeeeee-Nnnnnnnnnn-Uuuuuuuuuuu")

    }

    @Test
    fun test2(){
        doTest(
            "NyffsGeyylB" ,
            "N-Yy-Fff-Ffff-Sssss-Gggggg-Eeeeeee-Yyyyyyyy-Yyyyyyyyy-Llllllllll-Bbbbbbbbbbb")

    }

    @Test
    fun test3(){
        doTest(
            "" ,
            "")

    }


    private fun doTest(input:String, expect:String) {
        val result = tested.accum(input)
        assertEquals(expect, result)
    }
}