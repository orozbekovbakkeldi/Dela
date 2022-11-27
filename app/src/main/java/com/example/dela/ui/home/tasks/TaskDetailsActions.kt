package com.example.dela.ui.home.tasks

import com.example.dela.ui.model.task.AlarmInterval
import java.util.*

class TaskDetailsActions(
    val onTitleChanged: (String) -> Unit = {},
    val onDescriptionChanged: (String) -> Unit = {},
    val onCategoryChanged: (Long?) -> Unit = {},
    val onAlarmUpdate: (Calendar?) -> Unit = {},
    val onIntervalSelect: (AlarmInterval) -> Unit = {},
    val hasAlarmPermission: () -> Boolean = { false },
    val shouldCheckNotificationPermission: Boolean = false,
    val onUpPress: () -> Unit = {}
)