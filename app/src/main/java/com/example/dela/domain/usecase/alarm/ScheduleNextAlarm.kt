package com.example.dela.domain.usecase.alarm

import com.example.dela.domain.interactor.AlarmInteractor
import com.example.dela.domain.model.AlarmInterval
import com.example.dela.domain.model.Task
import com.example.dela.domain.provider.CalendarProvider
import com.example.dela.domain.repository.TaskRepo
import java.util.*

class ScheduleNextAlarm(
    private val taskRepository: TaskRepo,
    private val alarmInteractor: AlarmInteractor,
    private val calendarProvider: CalendarProvider
) {

    /**
     * Schedules the next alarm.
     *
     * @param task task to be rescheduled
     */
    suspend operator fun invoke(task: Task) {
        require(task.isRepeating) { "Task is not repeating" }
        require(task.dueDate != null) { "Task has no due date" }
        require(task.alarmInterval != null) { "Task has no alarm interval" }

        val currentTime = calendarProvider.getCurrentCalendar()
        do {
            updatedAlarmTime(task.dueDate, task.alarmInterval)
        } while (currentTime.after(task.dueDate))

        taskRepository.updateTask(task)
        alarmInteractor.schedule(task.id, task.dueDate.time.time)
    }

    private fun updatedAlarmTime(calendar: Calendar, alarmInterval: AlarmInterval) =
        when (alarmInterval) {
            AlarmInterval.HOURLY -> calendar.apply { add(Calendar.HOUR, 1) }
            AlarmInterval.DAILY -> calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }
            AlarmInterval.WEEKLY -> calendar.apply { add(Calendar.WEEK_OF_MONTH, 1) }
            AlarmInterval.MONTHLY -> calendar.apply { add(Calendar.MONTH, 1) }
            AlarmInterval.YEARLY -> calendar.apply { add(Calendar.YEAR, 1) }
        }

}