// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import de.dbn.oss.composables.CustomTooltipArea
import java.awt.Dimension

const val width = 280
const val height = 240

@Composable
fun BoardwalkTimer(onCloseRequest: () -> Unit) {
    var pinned by remember { mutableStateOf(true) }
    val windowState = rememberWindowState(size = DpSize(width = width.dp, height = height.dp))
    var darkMode by mutableStateOf(true)
    val timer = remember { Timer() }

    Window(
        onCloseRequest = onCloseRequest,
        alwaysOnTop = pinned,
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = pinned, onClick = { pinned = !pinned })
                            CustomTooltipArea(text = "Timer always on top") {
                                Text("Pinned")
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Light")
                            Switch(checked = darkMode, onCheckedChange = { darkMode = !darkMode })
                            Text("Dark")
                        }
                    }
                }
            }
        }
    }
}
