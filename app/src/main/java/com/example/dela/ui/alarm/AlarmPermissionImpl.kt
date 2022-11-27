package com.example.dela.ui.alarm

import android.os.Build

class AlarmPermissionImpl(private val permissionChecker: PermissionChecker) : AlarmPermission {

    override fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionChecker.canScheduleExactAlarms()
        } else {
            true
        }
    }

    override fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionChecker.checkPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
    }

    override fun shouldCheckNotificationPermission(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}