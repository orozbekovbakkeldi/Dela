package com.example.dela.ui.home.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dela.domain.usecase.CancelAlarm
import com.example.dela.domain.usecase.ScheduleAlarm
import com.example.dela.domain.usecase.UpdateTaskAsRepeating
import com.example.dela.ui.model.mapper.AlarmIntervalMapper
import com.example.dela.ui.model.task.AlarmInterval
import kotlinx.coroutines.launch
import java.util.*

class AlarmViewModel(
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val cancelAlarmUseCase: CancelAlarm,
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val updateTaskAsRepeatingUseCase: UpdateTaskAsRepeating
) : ViewModel() {


    fun updateAlarm(taskId: Long, alarm: Calendar?) = viewModelScope.launch {
        if (alarm != null) {
            scheduleAlarmUseCase(taskId, alarm)
        } else {
            cancelAlarmUseCase(taskId)
        }
    }

    fun setRepeating(taskId: Long, alarmInterval: AlarmInterval) = viewModelScope.launch {
        val interval = alarmIntervalMapper.mapToDomain(alarmInterval)
        updateTaskAsRepeatingUseCase(taskId, interval)
    }
}