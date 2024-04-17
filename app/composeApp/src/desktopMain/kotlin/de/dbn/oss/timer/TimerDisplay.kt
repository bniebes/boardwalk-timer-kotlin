// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.timer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dbn.oss.composables.CustomTooltipArea

private val YELLOW_600 = Color(0xFFFBC02D)
private val RED_600 = Color(0xFFE53935)

@Composable
fun TimerDisplay(
    timeOverall: String,
    timePerson: String,
    peopleRemaining: String,
    active: Boolean,
    paused: Boolean,
    timeStatePerson: TimeState,
    timeStateOverall: TimeState,
    onClickStart: (Int, Int, Int) -> Unit,
    onClickPause: () -> Unit,
    onClickNext: () -> Unit,
    onClickStop: () -> Unit,
    onNumberInput: (Int, Int, Int) -> Unit
) {
    var personCount by remember { mutableStateOf(8.toString()) }
    var interval by remember { mutableStateOf(120.toString()) }
    var additional by remember { mutableStateOf(180.toString()) }
    val timeHeight = 50.dp

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.height(timeHeight).fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimerText(text = timeOverall, timeState = timeStateOverall)
            }
            Divider(modifier = Modifier.height(timeHeight).width(1.dp))
            Column(
                modifier = Modifier.height(timeHeight).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimerText(text = timePerson, timeState = timeStatePerson)
            }
        }

        Divider()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(
                enabled = !active,
                onClick = {
                    onClickStart(
                        personCount.toInt(),
                        interval.toInt(),
                        additional.toInt()
                    )
                }) {
                Icon(painter = painterResource("timer_play.png"), contentDescription = "")
            }
            IconButton(enabled = active, onClick = onClickPause) {
                Icon(painter = painterResource("timer_pause.png"), contentDescription = "")
            }
            IconButton(enabled = active, onClick = onClickNext) {
                BadgedBox(badge = { Badge { Text(peopleRemaining) } }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
                }
            }
            IconButton(onClick = onClickStop) {
                Icon(painter = painterResource("timer_off.png"), contentDescription = "")
            }
        }
        Divider()
        Row(modifier = Modifier.padding(5.dp, 0.dp)) {
            NumberInput(
                modifier = Modifier.fillMaxWidth(0.3f),
                enabled = !paused && !active,
                value = personCount,
                onValueChange = { value ->
                    personCount = value.filter { it.isDigit() }
                    if (personCount != "") onNumberInput(personCount.toInt(), interval.toInt(), additional.toInt())
                },
                labelText = "People"
            )
            CustomTooltipArea(text = "Time in seconds") {
                Row {
                    NumberInput(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = interval,
                        enabled = !paused && !active,
                        onValueChange = { value ->
                            interval = value.filter { it.isDigit() }
                            if (interval != "") onNumberInput(personCount.toInt(), interval.toInt(), additional.toInt())
                        },
                        labelText = "Interval"
                    )
                    NumberInput(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !paused && !active,
                        value = additional,
                        onValueChange = { value ->
                            additional = value.filter { it.isDigit() }
                            if (additional != "")
                                onNumberInput(personCount.toInt(), interval.toInt(), additional.toInt())
                        },
                        labelText = "Additional"
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerText(text: String, timeState: TimeState) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        fontSize = 35.sp,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center,
        color = when(timeState) {
            TimeState.TIMELY -> MaterialTheme.colors.onSurface
            TimeState.LOW -> YELLOW_600
            TimeState.OVERTIME -> RED_600
        }
    )
}

@Composable
private fun NumberInput(
    modifier: Modifier,
    enabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String
) {
    OutlinedTextField(
        modifier = modifier,
        enabled = enabled,
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(labelText) })
}
