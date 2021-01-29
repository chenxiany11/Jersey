package edu.rosehulman.chenx11.jersey

data class Jersey(var name: String, var number: Int, var isRed: Boolean) {

    companion object {
        val PREFS = "PREFS"
        val KEY_JERSEY_NAME = "KEY_NAME"
        val KEY_NUMBER = "KEY_NUMBER"
        val KEY_IS_RED = "KEY_IS_RED"
    }
}