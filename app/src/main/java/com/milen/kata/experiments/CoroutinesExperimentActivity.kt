package com.milen.kata.experiments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.milen.kata.R
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class CoroutinesExperimentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines_experiment)

        findViewById<Button>(R.id.scenario_one).setOnClickListener {
            runScenarioOne()
        }

        findViewById<Button>(R.id.scenario_two).setOnClickListener {
            runScenarioTwo()
        }

        findViewById<Button>(R.id.scenario_three).setOnClickListener {
            runScenarioThree()
        }

        findViewById<Button>(R.id.scenario_four).setOnClickListener {
            runScenarioFour()
        }

        findViewById<Button>(R.id.scenario_five).setOnClickListener {
            runScenarioFive()
        }

        findViewById<Button>(R.id.scenario_cancel_one).setOnClickListener {
            runCancelScenarioOne()
        }

        findViewById<Button>(R.id.scenario_cancel_two).setOnClickListener {
            runCancelScenarioTwo()
        }

        findViewById<Button>(R.id.scenario_cancel_three).setOnClickListener {
            runCancelScenarioThree()
        }

        findViewById<Button>(R.id.scenario_cancel_four).setOnClickListener {
            runCancelScenarioFour()
        }

        findViewById<Button>(R.id.scenario_cancel_five).setOnClickListener {
            runCancelScenarioFive()
        }

        findViewById<Button>(R.id.scenario_cancel_six).setOnClickListener {
            runCancelScenarioSix()
        }
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L)
        return 2
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L)
        return 5
    }

    private fun runScenarioOne() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val time = measureTimeMillis {
                val one = doSomethingUsefulOne()
                val two = doSomethingUsefulTwo()
                println("The answer is ${one + two}")
            }
            println("Completed in $time ms")
            hideProgress()
        }
    }

    private fun runScenarioTwo() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val time = measureTimeMillis {
                val one = async { doSomethingUsefulOne() }
                val two = async { doSomethingUsefulTwo() }
                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")
            hideProgress()
        }
    }

    private fun runScenarioThree() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val time = measureTimeMillis {
                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                // some computation
                one.start() // start the first one
                two.start() // start the second one
                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")
            hideProgress()
        }
    }

    private fun runScenarioFour() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val time = measureTimeMillis {
                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }

                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")
            hideProgress()
        }
    }

    private fun runScenarioFive() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            try {
                failedConcurrentSum()
            } catch (e: ArithmeticException) {
                delay(500)
                println("Computation failed with ArithmeticException")
                hideProgress()
            }
        }
    }

    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async {
            try {
                delay(Long.MAX_VALUE) // Emulates very long computation
                100
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    private fun runCancelScenarioOne() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val startTime = System.currentTimeMillis()
            val job = launch {

                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin()
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private fun runCancelScenarioTwo() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // computation loop, just wastes CPU
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancel()
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private fun runCancelScenarioThree() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin()
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private fun runCancelScenarioFour() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (true) { // cancellable computation loop
                    // print a message twice a second
                    yield()
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private fun runCancelScenarioFive() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    println("job: I'm running finally")
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin()
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private fun runCancelScenarioSix() {
        GlobalScope.launch(Dispatchers.Default) {
            showProgress()
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("job: I'm running finally")
                        delay(1000L)
                        println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // delay a bit
            println("main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("main: Now I can quit.")
            hideProgress()
        }
    }

    private suspend fun Activity.showProgress(): Unit =
        withContext(Dispatchers.Main) {
            findViewById<View>(R.id.progress)?.visibility = View.VISIBLE
        }


    private suspend fun Activity.hideProgress(): Unit =
        withContext(Dispatchers.Main) {
            findViewById<View>(R.id.progress)?.visibility = View.GONE
        }
}