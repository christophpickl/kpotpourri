package com.github.christophpickl.kpotpourri.swing

import java.awt.Component
import java.awt.Font

// some button
//fun calcButtonFoobar(i: Int, j: Int) = i + j


/**
 * Changes
 */
fun Component.bold() = apply {
    font = font.deriveFont(Font.BOLD)
}
