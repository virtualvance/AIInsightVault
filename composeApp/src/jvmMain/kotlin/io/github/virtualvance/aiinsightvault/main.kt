package io.github.virtualvance.aiinsightvault

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AI Insight Vault",
    ) {
        App()
    }
}