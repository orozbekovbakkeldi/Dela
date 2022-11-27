package com.example.dela.ui.alarm

import com.example.dela.domain.interactor.NotificationInteractor
import com.example.dela.domain.model.Task
import com.example.dela.ui.model.mapper.TaskMapper

class NotificationInteractorImpl(
    private val taskNotification: TaskNotification,
    private val taskMapper: TaskMapper
) :
    NotificationInteractor {

    override fun show(task: Task) {
        if (task.isRepeating) {
            taskNotification.showRepeating(taskMapper.toUI(task))
        } else {
            taskNotification.show(taskMapper.toUI(task))
        }
    }

    override fun dismiss(notificationId: Long) {
        taskNotification.dismiss(notificationId)
    }
}