package com.example.sarahkhan_boggleapp

import android.widget.Button

data class Letter(
    val button: Button,
    val row: Int,
    val col: Int,
    var isSelected: Boolean = false
)