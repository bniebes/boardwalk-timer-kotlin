// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

const val width = 300
const val height = 250

@Composable
fun BoardwalkTimer(onCloseRequest: () -> Unit) {
    var alwaysOnTop by remember { mutableStateOf(true) }
    val windowState = rememberWindowState(size = DpSize(width = width.dp, height = height.dp))
    var darkMode by mutableStateOf(true)
    val timer = remember { Timer() }

    Window(
        onCloseRequest = onCloseRequest,
        alwaysOnTop = alwaysOnTop,
        title = "Boardwalk Timer",
        state = windowState,
    ) {
        window.minimumSize = Dimension(width, height)

        MaterialTheme(colors = if (darkMode) darkColors() else lightColors()) {
            Surface(Modifier.fillMaxSize()) {
                Column {
                    TimerDisplay(
                        timeOverall = timer.formattedTimeOverall,
                        timePerson = timer.formattedTimePerson,
                        peopleRemaining = timer.peopleRemaining,
                        timeStateOverall = timer.timeStateOverall,
                        timeStatePerson = timer.timeStatePerson,
                        active = timer.active,
                        paused = timer.paused,
                        onClickStart = timer::start,
                        onClickPause = timer::pause,
                        onClickNext = timer::next,
                        onClickStop = timer::reset,
                        onNumberInput = timer::set,
                    )
                    Divider()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LabeledCheckbox(
                            text = "Always on top",
                            checked = alwaysOnTop,
                            onCheckedChange = { alwaysOnTop = !alwaysOnTop })
                        LabeledCheckbox(
                            text = "Dark mode",
                            checked = darkMode,
                            onCheckedChange = { darkMode = !darkMode })
                    }
                }
            }
        }
    }
}

@Composable
fun LabeledCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text)
    }
}
