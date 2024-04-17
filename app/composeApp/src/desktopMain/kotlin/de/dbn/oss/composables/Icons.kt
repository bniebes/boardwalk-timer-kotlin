// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val sizeModifier = Modifier.size(32.dp)

@Composable
fun TimerStartIcon() {
    Icon(modifier = sizeModifier, painter = painterResource("timer_play.svg"), contentDescription = "")
}

@Composable
fun TimerPauseIcon() {
    Icon(modifier = sizeModifier, painter = painterResource("timer_pause.svg"), contentDescription = "")
}

@Composable
fun TimerNextIcon() {
    Icon(modifier = sizeModifier, painter = painterResource("next_plan.svg"), contentDescription = "")
}

@Composable
fun TimerStopIcon() {
    Icon(modifier = sizeModifier, painter = painterResource("timer_off.svg"), contentDescription = "")
}
