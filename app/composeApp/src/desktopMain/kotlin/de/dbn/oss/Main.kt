// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Boardwalk Timer") {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text("Hello, World!")
        }
    }
}