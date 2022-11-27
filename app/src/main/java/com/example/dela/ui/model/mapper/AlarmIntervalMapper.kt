package com.example.dela.ui.model.mapper

import com.example.dela.ui.model.task.AlarmInterval
import com.example.dela.domain.model.AlarmInterval as DomainAlarmInterval
import com.example.dela.ui.model.task.AlarmInterval as UIAlarmInterval

class AlarmIntervalMapper {

    fun mapToDomain(alarmInterval: AlarmInterval) = when (alarmInterval) {
        AlarmInterval.HOURLY -> DomainAlarmInterval.HOURLY
        AlarmInterval.DAILY -> DomainAlarmInterval.DAILY
        AlarmInterval.WEEKLY -> DomainAlarmInterval.WEEKLY
        AlarmInterval.MONTHLY -> DomainAlarmInterval.MONTHLY
        AlarmInterval.YEARLY -> DomainAlarmInterval.YEARLY
        AlarmInterval.NEVER -> null
    }

    fun mapToUI(alarmInterval: DomainAlarmInterval?) = when (alarmInterval) {
        DomainAlarmInterval.HOURLY -> UIAlarmInterval.HOURLY
        DomainAlarmInterval.DAILY -> UIAlarmInterval.DAILY
        DomainAlarmInterval.WEEKLY -> UIAlarmInterval.WEEKLY
        DomainAlarmInterval.MONTHLY -> UIAlarmInterval.MONTHLY
        DomainAlarmInterval.YEARLY -> UIAlarmInterval.YEARLY
        null -> UIAlarmInterval.NEVER
    }
}