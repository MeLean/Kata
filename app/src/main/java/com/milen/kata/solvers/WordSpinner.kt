package com.milen.kata.solvers

class WordSpinner() {
    /**
    * Write a function that takes in a string of one or more words, and returns the same string, but with all five or more letter words reversed (like the name of this kata).
    * Strings passed in will consist of only letters and spaces.
    * Spaces will be included only when more than one word is present.
    * Examples:
    *
    * spinWords("Hey fellow warriors") => "Hey wollef sroirraw"
    * spinWords("This is a test") => "This is a test"
    * spinWords("This is another test") => "This is rehtona test"
    * */

    fun spinWords(sentence: String): String {
        val arr = sentence.split(" ").toMutableList()
        var result = sentence
        for (i in 0 until arr.size) {
            if (arr[i].length >= 5) {
                result = result.replace(arr[i], arr[i].reversed(), false)
            }
        }

        return result
    }
}