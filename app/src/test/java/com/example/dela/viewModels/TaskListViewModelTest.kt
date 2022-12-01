package com.example.dela.viewModels

import com.example.dela.rules.CoroutineTestRule
import com.example.dela.domain.model.Category as DomainCategory
import com.example.dela.domain.model.Task as DomainTask
import com.example.dela.domain.model.TaskWithCategory as DomainTaskCategory
import com.example.dela.domain.usecase.LoadUnCompletedTasks
import com.example.dela.fake.UpdateTaskStatusFakeImpl
import com.example.dela.ui.home.tasks.TasksListViewModel
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.mapper.AlarmIntervalMapper
import com.example.dela.ui.model.mapper.CategoryMapper
import com.example.dela.ui.model.mapper.TaskMapper
import com.example.dela.ui.model.mapper.TaskWithCategoryMapper
import com.example.dela.ui.model.task.Task
import com.example.dela.ui.model.task.TaskWithCategory
import com.example.dela.ui.model.task.TasksListUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val loadUnCompletedTaskUseCase = mock<LoadUnCompletedTasks>()
    private val updateTaskStatusFake = UpdateTaskStatusFakeImpl()
    private val mapper = TaskWithCategoryMapper(TaskMapper(AlarmIntervalMapper()), CategoryMapper())

    private val viewModel = TasksListViewModel(loadUnCompletedTaskUseCase, updateTaskStatusFake, mapper)

    private val task1 = DomainTask(title = "Buy milk", dueDate = null)
    private val task2 = DomainTask(title = "Call Mark", dueDate = Calendar.getInstance())
    private val category = DomainCategory(name = "favorite", color = "#FF0000")


    @Test
    fun `if no tasks, Empty state must be returned`() = runTest {
        whenever(loadUnCompletedTaskUseCase.invoke(any())).doReturn(flow {
            emit(emptyList())
        })
        val state = viewModel.getTasksList(any()).first()
        assert(state is TasksListUIState.Empty)
    }

    @Test
    fun `if there are some tasks, Loaded state must be returned`() = runTest {
        whenever(loadUnCompletedTaskUseCase.invoke(any())).doReturn(flow {
            emit(listOf(DomainTaskCategory(task1, category), DomainTaskCategory(task2, category)))
        })
        val state = viewModel.getTasksList(any()).first()
        assert(state is TasksListUIState.Loaded)
    }

    @Test
    fun  `if there is an error, Error state must be returned`() = runTest {
        whenever(loadUnCompletedTaskUseCase.invoke(any())).doReturn(flow {
            throw IllegalStateException()
        })
        val state = viewModel.getTasksList(any()).first()
        assert(state is TasksListUIState.Error)
    }

    @Test
    fun `test if task is updated`() = runTest {
        val taskId = 1L
        val task = Task(id = taskId, title = "Hello")
        val category = Category(name = "Favorite", color = 1)
        val taskWithCategory = TaskWithCategory(task, category)
        viewModel.updateTaskStatus(taskWithCategory)
        assert(updateTaskStatusFake.isTaskUpdated(taskId))
    }





}