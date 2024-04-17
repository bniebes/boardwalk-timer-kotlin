// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss

import androidx.compose.ui.window.application
import de.dbn.oss.timer.BoardwalkTimer

fun main() = application {
    BoardwalkTimer(onCloseRequest = ::exitApplication)
}