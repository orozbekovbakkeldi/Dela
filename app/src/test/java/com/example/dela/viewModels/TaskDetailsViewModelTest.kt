package com.example.dela.viewModels

import com.example.dela.rules.CoroutineTestRule
import com.example.dela.domain.model.Task
import com.example.dela.domain.usecase.LoadTask
import com.example.dela.fake.CoroutinesDebouncerFake
import com.example.dela.fake.UpdateTaskCategoryFake
import com.example.dela.fake.UpdateTaskDescriptionFake
import com.example.dela.fake.UpdateTaskTitleFake
import com.example.dela.ui.home.tasks.TaskDetailsViewModel
import com.example.dela.ui.model.mapper.AlarmIntervalMapper
import com.example.dela.ui.model.mapper.TaskMapper
import com.example.dela.ui.model.task.TaskDetailState
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)

class TaskDetailsViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    private val loadTaskFake = mock<LoadTask>()
    private val updateTaskTitleFake = UpdateTaskTitleFake()
    private val updateTaskDescription = UpdateTaskDescriptionFake()
    private val updateTaskCategory = UpdateTaskCategoryFake()
    private val alarmIntervalMapper = AlarmIntervalMapper()
    private val taskMapper = TaskMapper(alarmIntervalMapper)
    private val debouncerFake = CoroutinesDebouncerFake()
    private val taskId = 1L
    private val categoryId = 1L;
    private val taskTitle = "TITLE_FAKE"
    private val taskDescription = "DESCRIPTION_FAKE"
    private val viewModel = TaskDetailsViewModel(
        loadTaskFake,
        taskMapper,
        debouncerFake,
        updateTaskTitleFake,
        updateTaskDescription,
        updateTaskCategory
    )


    @Test
    fun `check when task does not exist error state is returned`()  = runTest {
        whenever(loadTaskFake.invoke(taskId)).doReturn(null)
        val flow = viewModel.getTaskInfo(taskId)
        val state = flow.first()
        assertTrue(state is TaskDetailState.Error)
    }

    @Test
    fun `check when task does exist loaded state is returned`() = runTest {
        whenever(loadTaskFake.invoke(taskId)).doReturn(Task(title = ""))
        val flow = viewModel.getTaskInfo(taskId)
        assertTrue(flow.first() is TaskDetailState.Loaded)
    }

    @Test
    fun `check if task title is updated with new title`() = runTest {
        viewModel.updateTitle(taskId, taskTitle)
        assertTrue(updateTaskTitleFake.isTaskTitleUpdated(taskId))
        assertTrue(updateTaskTitleFake.getUpdateTaskTitle(taskId) == taskTitle)
    }

    @Test
    fun `check if task description is updated with new description`() = runTest {
        viewModel.updateDescription(taskId, taskDescription)
        assertTrue(updateTaskDescription.isDescriptionUpdated(taskId))
        assertTrue(updateTaskDescription.getUpdatedDescription(taskId) == taskDescription)
    }

    @Test
    fun  `check if task category is passed, category field of task is updated`() = runTest {
        viewModel.updateTaskCategory(taskId, categoryId)
        assertTrue(updateTaskCategory.isCategoryUpdated(taskId))
        assertTrue(updateTaskCategory.getUpdatedTaskCategory(taskId) == categoryId)
    }

    @Test
    fun `check if task category is null, nothing should happen`() = runTest {
        viewModel.updateTaskCategory(taskId)
        assertFalse(updateTaskCategory.isCategoryUpdated(taskId))
        assertNull(updateTaskCategory.getUpdatedTaskCategory(taskId))
    }
}