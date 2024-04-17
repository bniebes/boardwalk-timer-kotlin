// SPDX-License-Identifier: Apache-2.0

package de.dbn.oss.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Duration
import kotlin.math.abs


private const val DELAY_MILLIS = 1000L
private const val MILLIS_IN_SEC = 1000L
private const val MILLIS_IN_MIN = MILLIS_IN_SEC * 60

private const val DEFAULT_PEOPLE = 9
private const val DEFAULT_TIME_PERSON: Long = MILLIS_IN_MIN * 2
private const val DEFAULT_TIME_OVERALL = DEFAULT_TIME_PERSON * DEFAULT_PEOPLE
private const val DEFAULT_TIME_ADDITIONAL = MILLIS_IN_MIN * 3

private const val INITIAL_TIMER_VALUE = "00:00"
private const val INITIAL_PEOPLE_VALUE = "0"

private const val LOW_BOUND_PERSON = MILLIS_IN_SEC * 30
private const val LOW_BOUND_OVERALL = MILLIS_IN_MIN

enum class TimeState {
    TIMELY, LOW, OVERTIME
}

class Timer {
    private var coroutineScope = CoroutineScope(Dispatchers.Default)

    private var timeOverall = DEFAULT_TIME_OVERALL
    private var currentTimeOverall = timeOverall
    private var timePerson = DEFAULT_TIME_PERSON
    private var currentTimePerson = timePerson
    private var timeAdditional = DEFAULT_TIME_ADDITIONAL
    private var people = DEFAULT_PEOPLE
    private var currentPeopleRemaining = people

    var active by mutableStateOf(false)
    var paused by mutableStateOf(false)
    var formattedTimeOverall by mutableStateOf(INITIAL_TIMER_VALUE)
    var formattedTimePerson by mutableStateOf(INITIAL_TIMER_VALUE)
    var peopleRemaining by mutableStateOf(INITIAL_PEOPLE_VALUE)
    var timeStateOverall: TimeState by mutableStateOf(TimeState.TIMELY)
    var timeStatePerson: TimeState by mutableStateOf(TimeState.TIMELY)

    fun start(personCount: Int, interval: Int, additional: Int) {
        if (active) return
        if (!paused) {
            set(personCount, interval, additional)
        }
        active = true
        paused = false
        coroutineScope.launch {
            while (active) {
                delay(DELAY_MILLIS)
                currentTimeOverall -= DELAY_MILLIS
                currentTimePerson -= DELAY_MILLIS

                formattedTimeOverall = formatTime(currentTimeOverall)
                timeStateOverall = timeState(LOW_BOUND_OVERALL, currentTimeOverall)

                formattedTimePerson = formatTime(currentTimePerson)
                timeStatePerson = timeState(LOW_BOUND_PERSON, currentTimePerson)
            }
        }
    }

    fun pause() {
        active = false
        paused = true
    }

    fun next() {
        currentPeopleRemaining--
        when {
            currentPeopleRemaining == -1 ->  {
                currentTimePerson = timeAdditional
                peopleRemaining = "A"
            }
            currentPeopleRemaining < -1 -> currentTimePerson
            else -> {
                currentTimePerson = timePerson
                peopleRemaining = currentPeopleRemaining.toString()
            }
        }
    }

    fun reset() {
        coroutineScope.cancel()
        active = false
        paused = false
        coroutineScope = CoroutineScope(Dispatchers.Default)

        formattedTimeOverall = formatTime(currentTimeOverall)
        formattedTimePerson = formatTime(currentTimePerson)
        peopleRemaining = people.toString()
    }

    fun set(personCount: Int, interval: Int, additional: Int) {
        if (active) return

        timePerson = if (interval <= 0) DEFAULT_TIME_PERSON else MILLIS_IN_SEC * interval
        currentTimePerson = timePerson
        formattedTimePerson = formatTime(currentTimePerson)
        timeStatePerson = timeState(LOW_BOUND_PERSON, currentTimePerson)

        timeAdditional = MILLIS_IN_SEC * additional

        timeOverall = (timePerson * personCount) + timeAdditional
        currentTimeOverall = timeOverall
        formattedTimeOverall = formatTime(currentTimeOverall)
        timeStateOverall = timeState(LOW_BOUND_OVERALL, currentTimeOverall)

        people = personCount
        currentPeopleRemaining = people - 1
        peopleRemaining = currentPeopleRemaining.toString()

    }

    private fun formatTime(timeMillis: Long): String {
        val duration = Duration.ofMillis(timeMillis)
        val minutes = abs(duration.toMinutesPart()).padStartZeros()

        val seconds = duration.toSecondsPart()
        return if (seconds < 0) {
            "-${minutes}:${abs(seconds).padStartZeros()}"
        } else {
            "${minutes}:${seconds.padStartZeros()}"
        }
    }

    private fun Int.padStartZeros() = this.toString().padStart(length = 2, padChar = '0')

    private fun timeState(lowBound: Long, currentTimePerson: Long): TimeState {
        return when {
            currentTimePerson <= 0 -> TimeState.OVERTIME
            currentTimePerson <= lowBound -> TimeState.LOW
            else -> TimeState.TIMELY
        }
    }

}
