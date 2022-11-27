package com.example.dela.ui.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dela.domain.usecase.CompleteTask
import com.example.dela.domain.usecase.ShowAlarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
internal class TaskReceiver : BroadcastReceiver(), KoinComponent {

    private val coroutineScope: CoroutineScope by inject()

    private val completeTaskUseCase: CompleteTask by inject()

    private val showAlarmUseCase: ShowAlarm by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        coroutineScope.launch {
            handleIntent(intent)
        }
    }

    private suspend fun handleIntent(intent: Intent?) =
        when (intent?.action) {
            ALARM_ACTION -> getTaskId(intent)?.let { showAlarmUseCase(it) }
            COMPLETE_ACTION -> getTaskId(intent)?.let { completeTaskUseCase(it) }
            else -> Unit
        }

    private fun getTaskId(intent: Intent?) = intent?.getLongExtra(EXTRA_TASK, 0)

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"

        const val COMPLETE_ACTION = "com.escodro.alkaa.SET_COMPLETE"

        const val SNOOZE_ACTION = "com.escodro.alkaa.SNOOZE"
    }
}
