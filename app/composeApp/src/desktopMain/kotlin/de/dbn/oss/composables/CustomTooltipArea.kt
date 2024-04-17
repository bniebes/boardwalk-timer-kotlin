// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CustomTooltipArea(text: String, content: @Composable () -> Unit) {
    TooltipArea(
        content = content,
        tooltipPlacement = TooltipPlacement.ComponentRect(),
        tooltip = {
            Surface(color = MaterialTheme.colors.secondary) {
                Text(text = text)
            }
        }
    )
}