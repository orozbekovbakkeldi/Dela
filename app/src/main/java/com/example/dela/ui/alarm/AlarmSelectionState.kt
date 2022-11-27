package com.example.dela.ui.alarm

import androidx.compose.runtime.*
import com.example.dela.ui.model.task.AlarmInterval
import java.util.*

class AlarmSelectionState(calendar: Calendar?, alarmInterval: AlarmInterval?) {

    /**
     * The alarm date, if set.
     */
    var date by mutableStateOf(calendar)

    /**
     * The alarm data, if set.
     */
    var alarmInterval by mutableStateOf(alarmInterval)

    /**
     * The Exact Alarm permission dialog visibility state.
     */
    var showExactAlarmDialog by mutableStateOf(false)

    /**
     * The Notification permission dialog visibility state.
     */
    var showNotificationDialog by mutableStateOf(false)

    /**
     * The Notification Rationale dialog visibility state.
     */
    var showRationaleDialog by mutableStateOf(false)
}

@Composable
fun rememberAlarmSelectionState(calendar: Calendar?, alarmInterval: AlarmInterval?) =
    remember { AlarmSelectionState(calendar, alarmInterval) }