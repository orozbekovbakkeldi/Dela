package com.example.dela.domain.interactor

import com.example.dela.domain.model.Task

interface NotificationInteractor {

    /**
     * Shows an notification.
     *
     * @param task task to be shown as notification
     */
    fun show(task: Task)

    /**
     * Dismisses the current notification.
     *
     * @param notificationId notification to be dismissed
     */
    fun dismiss(notificationId: Long)
}
